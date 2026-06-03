package dev.pvpoptimizer.mixin;

import dev.pvpoptimizer.PvpOptimizerClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ClientPlayerEntityMixin {

    /**
     * method_6099 is the intermediary name for applyDamage in LivingEntity.
     * We guard inside the handler so only the local player triggers the reset.
     */
    @Inject(method = "method_6099", at = @At("HEAD"))
    private void onApplyDamage(DamageSource source, float amount, CallbackInfo ci) {
        // The mixin targets all LivingEntities, so filter to local player only.
        if ((Object) this instanceof ClientPlayerEntity self) {
            PvpOptimizerClient.controller().onLocalPlayerDamaged(self, source, amount);
        }
    }
}
