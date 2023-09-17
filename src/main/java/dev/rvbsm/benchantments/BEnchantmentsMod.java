package dev.rvbsm.benchantments;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class BEnchantmentsMod implements ModInitializer {

	private static final String MOD_ID = "better-enchantments";
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".toml");
	public static final ModConfig CONFIG = new ModConfig(CONFIG_PATH);

	@Override
	public void onInitialize() {
		CONFIG.load();
	}
}
