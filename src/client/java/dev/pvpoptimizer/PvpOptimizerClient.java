package dev.pvpoptimizer;

import dev.pvpoptimizer.client.JumpResetController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PvpOptimizerClient implements ClientModInitializer {
    public static final String MOD_ID = "pvpoptimizer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final JumpResetController CONTROLLER = new JumpResetController();

    @Override
    public void onInitializeClient() {
        // Tick hook: executes any pending jump on the correct tick.
        ClientTickEvents.END_CLIENT_TICK.register(CONTROLLER::onClientTick);

        // Clean up state on disconnect.
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> CONTROLLER.reset());
    }

    public static JumpResetController controller() {
        return CONTROLLER;
    }
}
