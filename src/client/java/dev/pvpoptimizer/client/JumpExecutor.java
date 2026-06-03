package dev.pvpoptimizer.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public final class JumpExecutor {

    public boolean canJump(MinecraftClient client, ClientPlayerEntity player) {
        // No menus open, must be grounded, not in fluid, not on a ladder/elytra.
        return client.currentScreen == null
            && player.isOnGround()
            && !player.isTouchingWater()   // fluid kills the momentum math
            && !player.isInLava()          // same reason
            && !player.isClimbing()
            && !player.isGliding();
    }

    public void jump(ClientPlayerEntity player) {
        // player.jump() sets vy = 0.42 and handles the sprint boost internally.
        // It does NOT hold the spacebar, so jumpTicks is managed correctly by
        // the engine and won't stick for subsequent ticks.
        player.jump();
    }
}
