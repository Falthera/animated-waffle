package dev.pvpoptimizer.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;

public final class DamageValidator {
	public boolean isCombatDamage(ClientPlayerEntity player, DamageSource source) {
		if (source == null) {
			return true;
		}

		var attacker = source.getAttacker();
		if (attacker == null) {
			return false;
		}

		if (!(attacker instanceof net.minecraft.entity.LivingEntity)) {
			return false;
		}

		var directSource = source.getSource();
		if (attacker == player || directSource == player) {
			return false;
		}

		return true;
	}
}
