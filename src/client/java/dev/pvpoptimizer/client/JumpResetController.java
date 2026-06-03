package dev.pvpoptimizer.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;

public final class JumpResetController {

    // Must fire on tick 0 — no delay.
    private static final int JUMP_DELAY_TICKS = 0;
    // Prevent re-triggering within the same hurt window.
    private static final int COOLDOWN_TICKS = 10;

    private final CooldownManager cooldownManager = new CooldownManager();
    private final JumpTimingEngine jumpTimingEngine = new JumpTimingEngine();
    private final JumpExecutor jumpExecutor = new JumpExecutor();
    private final DamageValidator damageValidator = new DamageValidator();
    private final DebugLogger debugLogger = new DebugLogger();

    public void reset() {
        cooldownManager.reset();
        jumpTimingEngine.reset();
    }

    /**
     * Called by the mixin the moment the local player takes damage.
     * This is the reactive path — fires in response to actual knockback.
     */
    public void onLocalPlayerDamaged(ClientPlayerEntity player,
                                     DamageSource source, float amount) {
        if (player == null) return;
        if (!damageValidator.isCombatDamage(player, source)) {
            debugLogger.damage("ignored: non-combat source");
            return;
        }

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
        // Schedule for tick 0 — same tick the knockback lands.
        jumpTimingEngine.schedule(currentTick + JUMP_DELAY_TICKS);

        debugLogger.damage("reactive jump scheduled at tick " + currentTick);
        debugLogger.schedule(currentTick + JUMP_DELAY_TICKS);
    }

    /**
     * Called every client tick (END_CLIENT_TICK).
     * Executes the scheduled jump as soon as the player is in a valid state.
     */
    public void onClientTick(MinecraftClient client) {
        if (!jumpTimingEngine.hasPendingJump()) return;

        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            jumpTimingEngine.reset();
            return;
        }

        int currentTick = player.age;
        if (!jumpTimingEngine.shouldExecute(currentTick)) return;

        if (!jumpExecutor.canJump(client, player)) {
            debugLogger.jump("state invalid — in air, water, or menu; cancelling");
            jumpTimingEngine.reset();
            return;
        }

        jumpExecutor.jump(player);
        debugLogger.jump("reactive jump executed on tick " + currentTick);
        jumpTimingEngine.reset();
    }

    /**
     * Kept so PvpOptimizerClient compiles, but predictive jumping is removed.
     */
    public void triggerPredictiveJump(ClientPlayerEntity player) {
        // No-op. Predictive pre-jumping fires before knockback lands and
        // therefore cannot cancel backward momentum. Use the damage path instead.
    }
}
