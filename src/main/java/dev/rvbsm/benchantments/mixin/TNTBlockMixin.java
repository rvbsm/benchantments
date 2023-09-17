package dev.rvbsm.benchantments.mixin;

import dev.rvbsm.benchantments.BEnchantmentsMod;
import dev.rvbsm.benchantments.EnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TntBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TntBlock.class)
public abstract class TNTBlockMixin extends Block {

	public TNTBlockMixin(Settings settings) {
		super(settings);
	}

	@Shadow
	private static void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		if (BEnchantmentsMod.CONFIG.isFlintAndSword() && EnchantmentHelper.hasFireAspect(player)) primeTnt(world, pos, player);
		else super.afterBreak(world, player, pos, state, blockEntity, tool);
	}
}
