package dev.pvpoptimizer.mixin;

import dev.pvpoptimizer.PvpOptimizerClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    /**
     * Inject at the top of applyDamage so we fire on the exact tick
     * the engine processes the hit — this is tick-0 of the knockback.
     */
    @Inject(method = "applyDamage", at = @At("HEAD"))
    private void onApplyDamage(DamageSource source, float amount, CallbackInfo ci) {
        ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
        PvpOptimizerClient.controller().onLocalPlayerDamaged(self, source, amount);
    }
}
