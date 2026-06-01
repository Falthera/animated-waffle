package dev.pvpoptimizer.mixin;

// This file is intentionally left as a no-op placeholder.
// Damage detection has been moved to PvpOptimizerClient
// using the Fabric Entity Damage Events API.
// This class must still exist because pvpoptimizer.mixins.json references it.

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
}
