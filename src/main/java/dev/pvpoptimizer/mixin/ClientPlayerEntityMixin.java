package dev.pvpoptimizer.mixin;

import dev.pvpoptimizer.PvpOptimizerClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
	@Inject(method = "damage", at = @At("HEAD"))
	private void pvpoptimizer$onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		PvpOptimizerClient.controller().onLocalPlayerDamaged((ClientPlayerEntity) (Object) this, source, amount);
	}
}
