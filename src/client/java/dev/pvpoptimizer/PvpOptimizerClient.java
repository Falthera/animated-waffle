package dev.pvpoptimizer;

import dev.pvpoptimizer.client.JumpResetController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class PvpOptimizerClient implements ClientModInitializer {
	public static final String MOD_ID = "pvpoptimizer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final JumpResetController CONTROLLER = new JumpResetController();

	// Opponent must be this charged (0.0–1.0) before we pre-jump.
	private static final float COOLDOWN_THRESHOLD = 0.85f;

	// Scan radius in blocks — standard sword reach is ~3.
	private static final double THREAT_RADIUS = 4.0;

	// Rising-edge tracker: only fire once per swing, not every tick while charged.
	private boolean wasAboveThreshold = false;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			ClientPlayerEntity player = client.player;
			if (player == null || client.world == null || client.currentScreen != null) {
				wasAboveThreshold = false;
				return;
			}

			boolean anyAboveThreshold = isAnyOpponentCharged(client, player);

			if (anyAboveThreshold && !wasAboveThreshold) {
				CONTROLLER.triggerPredictiveJump(player);
			}

			wasAboveThreshold = anyAboveThreshold;
		});

		ClientTickEvents.END_CLIENT_TICK.register(CONTROLLER::onClientTick);

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			wasAboveThreshold = false;
			CONTROLLER.reset();
		});
	}

	private boolean isAnyOpponentCharged(MinecraftClient client, ClientPlayerEntity localPlayer) {
		Box searchBox = localPlayer.getBoundingBox().expand(THREAT_RADIUS);
		List<PlayerEntity> nearby = client.world.getEntitiesByClass(
				PlayerEntity.class,
				searchBox,
				p -> p != localPlayer && !p.isSpectator()
		);

		for (PlayerEntity opponent : nearby) {
			if (!isFacingUs(opponent, localPlayer)) {
				continue;
			}
			if (opponent.getAttackCooldownProgress(0f) >= COOLDOWN_THRESHOLD) {
				return true;
			}
		}
		return false;
	}

	private boolean isFacingUs(PlayerEntity opponent, ClientPlayerEntity localPlayer) {
		double dx = localPlayer.getX() - opponent.getX();
		double dz = localPlayer.getZ() - opponent.getZ();
		double dist = Math.sqrt(dx * dx + dz * dz);
		if (dist < 0.01) return true;
		dx /= dist;
		dz /= dist;
		var look = opponent.getRotationVec(1.0f);
		return (dx * look.x + dz * look.z) > 0.5;
	}

	public static JumpResetController controller() {
		return CONTROLLER;
	}
}
