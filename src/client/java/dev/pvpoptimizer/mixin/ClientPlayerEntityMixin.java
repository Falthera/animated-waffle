package dev.pvpoptimizer.mixin;

import dev.pvpoptimizer.PvpOptimizerClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ClientPlayerEntityMixin {

    // No parameters declared — Mixin matches by method name alone and injects
    // without us needing to type any of the obfuscated parameter classes.
    @Inject(method = "method_6099", at = @At("HEAD"))
    private void onApplyDamage(CallbackInfo ci) {
        if ((Object) this instanceof ClientPlayerEntity self) {
            PvpOptimizerClient.controller().onLocalPlayerDamaged(self);
        }
    }
}
