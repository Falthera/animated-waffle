package dev.pvpoptimizer.mixin;

// Empty placeholder — damage detection was moved to hurtTime polling in PvpOptimizerClient.
// This class must still exist because pvpoptimizer.mixins.json references it.

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
}
