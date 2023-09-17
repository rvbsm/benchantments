package dev.rvbsm.benchantments.mixin;

import dev.rvbsm.benchantments.BEnchantmentsMod;
import dev.rvbsm.benchantments.EnchantmentHelper;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {

	@Shadow
	private int amount;

	@Shadow
	protected abstract int getMendingRepairAmount(int experienceAmount);

	@Shadow
	protected abstract int getMendingRepairCost(int repairAmount);

	@Inject(method = "repairPlayerGears", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void repairPlayerGears(PlayerEntity player, int amount, CallbackInfoReturnable<Integer> cir) {
		if (BEnchantmentsMod.CONFIG.isFullMending()) cir.setReturnValue(repairAllPlayerGears(player, amount));
	}

	@Unique
	private int repairAllPlayerGears(PlayerEntity player, int amount) {
		final ItemStack damagedItem = EnchantmentHelper.getRepairableItem(player);
		if (damagedItem == null) return amount;

		final int itemDamage = damagedItem.getDamage();
		final int repairAmount = Math.min(this.getMendingRepairAmount(this.amount), itemDamage);
		damagedItem.setDamage(itemDamage - repairAmount);
		final int remainder = amount - this.getMendingRepairCost(repairAmount);
		if (remainder > 0) return this.repairAllPlayerGears(player, remainder);

		return 0;
	}
}
