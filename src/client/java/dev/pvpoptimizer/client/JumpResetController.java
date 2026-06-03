package dev.pvpoptimizer.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public final class JumpResetController {

    private static final int JUMP_DELAY_TICKS = 0;
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

    public void onLocalPlayerDamaged(ClientPlayerEntity player) {
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

        debugLogger.damage("reactive jump scheduled at tick " + currentTick);
        debugLogger.schedule(currentTick + JUMP_DELAY_TICKS);
    }

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

    // No-op — kept so PvpOptimizerClient compiles.
    public void triggerPredictiveJump(ClientPlayerEntity player) {}
}
