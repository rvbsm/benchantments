package dev.rvbsm.benchantments.mixin;

import dev.rvbsm.benchantments.BEnchantmentsMod;
import dev.rvbsm.benchantments.EnchantmentHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends ToolItem {

	public SwordItemMixin(ToolMaterial material, Settings settings) {
		super(material, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (BEnchantmentsMod.CONFIG.isFlintAndSword()) {
			final PlayerEntity player = context.getPlayer();
			final World world = context.getWorld();
			final BlockPos blockPos = context.getBlockPos();
			final BlockState blockState = world.getBlockState(blockPos);

			final boolean canBeLit = blockState.getProperties().contains(Properties.LIT);
			if (canBeLit && !blockState.get(Properties.LIT) && EnchantmentHelper.hasFireAspect(player)) {
				world.playSound(null, blockPos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS);
				world.setBlockState(blockPos, blockState.with(Properties.LIT, true));
				world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);

				return ActionResult.SUCCESS;
			}
		}

		return super.useOnBlock(context);
	}
}
