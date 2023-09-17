package dev.rvbsm.benchantments.mixin;

import dev.rvbsm.benchantments.BEnchantmentsMod;
import dev.rvbsm.benchantments.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {

	@Shadow
	private boolean dealtDamage;

	protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F"))
	private float onEntityHit$getAttackDamage(ItemStack stack, EntityGroup group, EntityHitResult entityHitResult) {
		if (BEnchantmentsMod.CONFIG.isBedrockImpaling())
			return EnchantmentHelper.getAttackDamage(stack, entityHitResult.getEntity());

		return EnchantmentHelper.getAttackDamage(stack, group);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void tick$dealtDamage(CallbackInfo ci) {
		if (BEnchantmentsMod.CONFIG.isVoidLoyalty() && this.getY() < this.getWorld().getBottomY()) this.dealtDamage = true;
	}
}
