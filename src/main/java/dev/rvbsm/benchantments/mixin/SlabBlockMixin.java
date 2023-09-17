package dev.rvbsm.benchantments.mixin;

import dev.rvbsm.benchantments.BEnchantmentsMod;
import dev.rvbsm.benchantments.EnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SlabBlock.class)
public abstract class SlabBlockMixin extends Block {

	@Unique
	private HitResult breakResult;

	public SlabBlockMixin(Settings settings) {
		super(settings);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		this.breakResult = player.raycast(5, 0f, false);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		if (BEnchantmentsMod.CONFIG.isSilkSlabs() && player.isSneaking() && EnchantmentHelper.hasSilkTouch(tool) && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
			player.incrementStat(Stats.MINED.getOrCreateStat(this));
			player.addExhaustion(0.005f);

			final Vec3d hitPos = this.breakResult.getPos();
			final boolean isTopBroken = hitPos.getY() - pos.getY() >= .5d;
			world.setBlockState(pos, state.with(Properties.SLAB_TYPE, isTopBroken ? SlabType.BOTTOM : SlabType.TOP));
			Block.dropStacks(state.with(Properties.SLAB_TYPE, isTopBroken ? SlabType.TOP : SlabType.BOTTOM), world, pos, blockEntity, player, tool);
		} else super.afterBreak(world, player, pos, state, blockEntity, tool);
	}
}
