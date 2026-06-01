package dev.pvpoptimizer.client;

public final class CooldownManager {
	private int nextAllowedTick = Integer.MIN_VALUE;

	public boolean canSchedule(int currentTick) {
		return currentTick >= nextAllowedTick;
	}

	public void arm(int nextTick) {
		nextAllowedTick = nextTick;
	}

	public void reset() {
		nextAllowedTick = Integer.MIN_VALUE;
	}
}