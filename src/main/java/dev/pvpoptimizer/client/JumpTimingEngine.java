package dev.pvpoptimizer.client;

public final class JumpTimingEngine {
	private int scheduledTick = Integer.MIN_VALUE;

	public void schedule(int tick) {
		scheduledTick = tick;
	}

	public boolean hasPendingJump() {
		return scheduledTick != Integer.MIN_VALUE;
	}

	public boolean shouldExecute(int currentTick) {
		return hasPendingJump() && currentTick >= scheduledTick;
	}

	public void reset() {
		scheduledTick = Integer.MIN_VALUE;
	}
}
