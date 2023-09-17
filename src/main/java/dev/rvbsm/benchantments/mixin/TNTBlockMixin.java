package dev.rvbsm.benchantments.mixin;

import dev.rvbsm.benchantments.BEnchantmentsMod;
import dev.rvbsm.benchantments.EnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TntBlock.class)
public abstract class TNTBlockMixin extends Block {

	public TNTBlockMixin(Settings settings) {
		super(settings);
	}

	@Shadow
	private static void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {}

	@Inject(method = "onUse", at = @At("TAIL"), cancellable = true)
	public void onUse$isOf(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if (BEnchantmentsMod.CONFIG.isFlintAndSword() && EnchantmentHelper.hasFireAspect(player)) {
			primeTnt(world, pos, player);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
			cir.setReturnValue(ActionResult.success(world.isClient));
		}
	}
}
