package dev.pvpoptimizer.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;

public final class JumpResetController {
	private static final int JUMP_DELAY_TICKS = 1;
	private static final int COOLDOWN_TICKS = 1;
	private static final double KNOCKBACK_THRESHOLD = 0.0009D;

	private final DamageValidator damageValidator = new DamageValidator();
	private final CooldownManager cooldownManager = new CooldownManager();
	private final JumpTimingEngine jumpTimingEngine = new JumpTimingEngine();
	private final JumpExecutor jumpExecutor = new JumpExecutor();
	private final DebugLogger debugLogger = new DebugLogger();

	private int damageTick = Integer.MIN_VALUE;
	private double damageVelocityX;
	private double damageVelocityZ;

	public void reset() {
		cooldownManager.reset();
		jumpTimingEngine.reset();
		damageTick = Integer.MIN_VALUE;
		damageVelocityX = 0.0D;
		damageVelocityZ = 0.0D;
	}

	public void onLocalPlayerDamaged(ClientPlayerEntity player, DamageSource source, float amount) {
		if (player == null || source == null) {
			return;
		}

		int currentTick = player.age;
		if (!damageValidator.isCombatDamage(player, source)) {
			debugLogger.damage("ignored non-combat source");
			return;
		}

		if (!cooldownManager.canSchedule(currentTick)) {
			debugLogger.cooldown("blocked duplicate activation");
			return;
		}

		if (jumpTimingEngine.hasPendingJump()) {
			debugLogger.cooldown("jump already pending");
			return;
		}

		damageTick = currentTick;
		damageVelocityX = player.getVelocity().x;
		damageVelocityZ = player.getVelocity().z;
		cooldownManager.arm(currentTick + COOLDOWN_TICKS);
		jumpTimingEngine.schedule(currentTick + JUMP_DELAY_TICKS);

		debugLogger.damage("combat hit accepted");
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
			debugLogger.jump("state invalid");
			jumpTimingEngine.reset();
			return;
		}

		if (!hasKnockback(player)) {
			debugLogger.knockback("no verified knockback");
			jumpTimingEngine.reset();
			return;
		}

		jumpExecutor.jump(player);
		debugLogger.jump("jump executed");
		jumpTimingEngine.reset();
	}

	private boolean hasKnockback(ClientPlayerEntity player) {
		if (damageTick == Integer.MIN_VALUE) {
			return false;
		}

		if (player.age - damageTick > 3) {
			return false;
		}

		double deltaX = player.getVelocity().x - damageVelocityX;
		double deltaZ = player.getVelocity().z - damageVelocityZ;
		double horizontalDelta = (deltaX * deltaX) + (deltaZ * deltaZ);
		return horizontalDelta >= KNOCKBACK_THRESHOLD;
	}
}