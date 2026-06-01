package dev.pvpoptimizer.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public final class JumpExecutor {
	public boolean canJump(MinecraftClient client, ClientPlayerEntity player) {
		return client.currentScreen == null
			&& player.isOnGround()
			&& !player.isTouchingWater()
			&& !player.isClimbing()
			&& !player.isFallFlying();
	}

	public void jump(ClientPlayerEntity player) {
		player.jump();
	}
}