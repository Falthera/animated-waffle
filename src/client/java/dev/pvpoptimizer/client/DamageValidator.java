package dev.pvpoptimizer.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public final class DamageValidator {
	public boolean isCombatDamage(ClientPlayerEntity player, DamageSource source) {
		Entity attacker = source.getAttacker();
		Entity directSource = source.getSource();

		if (attacker == null) {
			return false;
		}

		if (!(attacker instanceof LivingEntity)) {
			return false;
		}

		if (attacker == player || directSource == player) {
			return false;
		}

		return true;
	}
}