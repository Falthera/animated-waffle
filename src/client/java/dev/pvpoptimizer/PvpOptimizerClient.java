package dev.pvpoptimizer;

import dev.pvpoptimizer.client.JumpResetController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntityDamageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PvpOptimizerClient implements ClientModInitializer {
	public static final String MOD_ID = "pvpoptimizer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final JumpResetController CONTROLLER = new JumpResetController();

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(CONTROLLER::onClientTick);
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> CONTROLLER.reset());

		EntityDamageEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.player != null && entity == client.player) {
				CONTROLLER.onLocalPlayerDamaged((ClientPlayerEntity) entity, source, amount);
			}
			return true;
		});
	}

	public static JumpResetController controller() {
		return CONTROLLER;
	}
}
