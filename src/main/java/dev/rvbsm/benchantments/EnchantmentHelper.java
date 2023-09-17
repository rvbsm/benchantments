package dev.rvbsm.benchantments;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class EnchantmentHelper extends net.minecraft.enchantment.EnchantmentHelper {

	public static float getAttackDamage(ItemStack stack, Entity target) {
		if (!target.isLiving()) return getAttackDamage(stack, EntityGroup.DEFAULT);
		else if (target.isTouchingWaterOrRain()) return getAttackDamage(stack, EntityGroup.AQUATIC);

		return getAttackDamage(stack, ((LivingEntity) target).getGroup());
	}

	public static int getFeatherFalling(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.FEATHER_FALLING, entity);
	}

	public static boolean hasMending(ItemStack stack) {
		return getLevel(Enchantments.MENDING, stack) > 0;
	}

	public static boolean hasFireAspect(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.FIRE_ASPECT, entity) > 0;
	}

	public static @Nullable ItemStack getRepairableItem(PlayerEntity player) {
		final PlayerInventory inventory = player.getInventory();
		if (inventory.isEmpty()) return null;

		final List<ItemStack> repairableItems = Stream.of(inventory.main, inventory.armor, inventory.offHand)
						.flatMap(List::stream)
						.filter(ItemStack::isDamaged)
						.filter(EnchantmentHelper::hasMending)
						.toList();
		if (repairableItems.isEmpty()) return null;
		return repairableItems.get(player.getRandom().nextInt(repairableItems.size()));
	}
}
