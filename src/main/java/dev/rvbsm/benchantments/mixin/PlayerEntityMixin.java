package dev.rvbsm.benchantments.mixin;

import dev.rvbsm.benchantments.BEnchantmentsMod;
import dev.rvbsm.benchantments.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "attack", at = @At(value = "STORE"), index = 3)
	private float attack$damage(float value, Entity target) {
		if (BEnchantmentsMod.CONFIG.isBedrockImpaling())
			return EnchantmentHelper.getAttackDamage(this.getMainHandStack(), target);

		return value;
	}
}
