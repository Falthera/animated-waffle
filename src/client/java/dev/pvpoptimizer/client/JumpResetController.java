package dev.pvpoptimizer.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;

public final class JumpResetController {
	private static final int JUMP_DELAY_TICKS = 0;
	private static final int COOLDOWN_TICKS = 10;

	private final CooldownManager cooldownManager = new CooldownManager();
	private final JumpTimingEngine jumpTimingEngine = new JumpTimingEngine();
	private final JumpExecutor jumpExecutor = new JumpExecutor();
	private final DebugLogger debugLogger = new DebugLogger();

	public void reset() {
		cooldownManager.reset();
		jumpTimingEngine.reset();
	}

	/**
	 * Called by the old damage path (kept for compatibility, no longer used).
	 */
	public void onLocalPlayerDamaged(ClientPlayerEntity player, DamageSource source, float amount) {
		// No-op: predictive jump is handled by triggerPredictiveJump.
	}

	/**
	 * Called when an opponent's attack cooldown crosses the threshold.
	 */
	public void triggerPredictiveJump(ClientPlayerEntity player) {
		if (player == null) return;

		int currentTick = player.age;

		if (!cooldownManager.canSchedule(currentTick)) {
			debugLogger.cooldown("blocked by cooldown");
			return;
		}

		if (jumpTimingEngine.hasPendingJump()) {
			debugLogger.cooldown("jump already pending");
			return;
		}

		cooldownManager.arm(currentTick + COOLDOWN_TICKS);
		jumpTimingEngine.schedule(currentTick + JUMP_DELAY_TICKS);

		debugLogger.damage("predictive jump scheduled");
		debugLogger.schedule(currentTick + JUMP_DELAY_TICKS);
	}

	public void onClientTick(MinecraftClient client) {
		if (!jumpTimingEngine.hasPendingJump()) {
			return;
		}

		ClientPlayerEntity player = client.player;
		if (player == null || client.world == null) {
			jumpTimingEngine.reset();
			return;
		}

		int currentTick = player.age;
		if (!jumpTimingEngine.shouldExecute(currentTick)) {
			return;
		}

		if (!jumpExecutor.canJump(client, player)) {
			debugLogger.jump("state invalid, cancelling");
			jumpTimingEngine.reset();
			return;
		}

		jumpExecutor.jump(player);
		debugLogger.jump("predictive jump executed");
		jumpTimingEngine.reset();
	}
}
