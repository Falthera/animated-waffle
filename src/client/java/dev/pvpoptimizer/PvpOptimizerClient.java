package dev.pvpoptimizer;

import dev.pvpoptimizer.client.JumpResetController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PvpOptimizerClient implements ClientModInitializer {
	public static final String MOD_ID = "pvpoptimizer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final JumpResetController CONTROLLER = new JumpResetController();

	private int lastHurtTime = 0;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			ClientPlayerEntity player = client.player;
			if (player != null && client.world != null) {
				int hurtTime = player.hurtTime;
				// hurtTime resets to its max value (10) when the player is hit
				if (hurtTime > lastHurtTime) {
					CONTROLLER.onLocalPlayerDamaged(player, null, 0f);
				}
				lastHurtTime = hurtTime;
			}
			CONTROLLER.onClientTick(client);
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			lastHurtTime = 0;
			CONTROLLER.reset();
		});
	}

	public static JumpResetController controller() {
		return CONTROLLER;
	}
}
