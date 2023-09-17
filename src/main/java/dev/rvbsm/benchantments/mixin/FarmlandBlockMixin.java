package dev.rvbsm.benchantments.mixin;

import dev.rvbsm.benchantments.BEnchantmentsMod;
import dev.rvbsm.benchantments.EnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin extends Block {

	@Unique
	private float fallDistanceMultiplier = 1;

	public FarmlandBlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "onLandedUpon", at = @At("HEAD"))
	private void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
		if (BEnchantmentsMod.CONFIG.isFeatherFarmlands() && entity instanceof LivingEntity livingEntity) {
			final int featherFallingLevel = EnchantmentHelper.getFeatherFalling(livingEntity);
			this.fallDistanceMultiplier = (100 - (float) featherFallingLevel * 24) / 100;
		}
	}

	@ModifyVariable(method = "onLandedUpon", at = @At("HEAD"), argsOnly = true)
	private float onLandedUpon$fallDistance(float fallDistance) {
		return fallDistance * this.fallDistanceMultiplier;
	}

	@ModifyArg(method = "onLandedUpon", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"), index = 4)
	private float super$onLandedUpon(float fallDistance) {
		return fallDistance / this.fallDistanceMultiplier;
	}
}
