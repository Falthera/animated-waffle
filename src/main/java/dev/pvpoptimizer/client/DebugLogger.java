package dev.pvpoptimizer.client;

import dev.pvpoptimizer.PvpOptimizerClient;

public final class DebugLogger {
	private static final boolean DEBUG = false;

	public void damage(String message) {
		if (DEBUG) {
			PvpOptimizerClient.LOGGER.info("[DAMAGE] {}", message);
		}
	}

	public void knockback(String message) {
		if (DEBUG) {
			PvpOptimizerClient.LOGGER.info("[KNOCKBACK] {}", message);
		}
	}

	public void cooldown(String message) {
		if (DEBUG) {
			PvpOptimizerClient.LOGGER.info("[COOLDOWN] {}", message);
		}
	}

	public void schedule(int scheduledTick) {
		if (DEBUG) {
			PvpOptimizerClient.LOGGER.info("[SCHEDULE] jump at tick {}", scheduledTick);
		}
	}

	public void jump(String message) {
		if (DEBUG) {
			PvpOptimizerClient.LOGGER.info("[JUMP] {}", message);
		}
	}
}
