package dev.pvpoptimizer.mixin;

import dev.pvpoptimizer.PvpOptimizerClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Inject(method = "method_6099", at = @At("HEAD"))
    private void onApplyDamage(DamageRecord record, DamageSource source, boolean blocked, CallbackInfo ci) {
        if ((Object) this instanceof ClientPlayerEntity self) {
            PvpOptimizerClient.controller().onLocalPlayerDamaged(self, source);
        }
    }
}
