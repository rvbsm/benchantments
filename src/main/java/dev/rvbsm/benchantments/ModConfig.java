package dev.rvbsm.benchantments;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModConfig {
	private final Path path;

	@Getter
	private boolean featherFarmlands = true;
	@Getter
	private boolean silkSlabs = true;
	@Getter
	private boolean flintAndSword = true;
	@Getter
	private boolean bedrockImpaling = true;
	@Getter
	private boolean voidLoyalty = true;
	@Getter
	private boolean fullMending = true;

	public ModConfig(Path path) {
		this.path = path;
	}

	public void save() {
		try (var writer = Files.newBufferedWriter(this.path)) {
			final Map<String, Boolean> entries = new LinkedHashMap<>();
			entries.put("feather_farmlands", this.featherFarmlands);
			entries.put("silk_slabs", this.silkSlabs);
			entries.put("flint_and_sword", this.flintAndSword);
			entries.put("bedrock_impaling", this.bedrockImpaling);
			entries.put("void_loyalty", this.voidLoyalty);
			entries.put("full_mending", this.fullMending);

			new TomlWriter().write(entries, writer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void load() {
		if (Files.exists(this.path)) try (var reader = Files.newBufferedReader(this.path)) {
			final Toml toml = new Toml().read(reader);

			this.featherFarmlands = toml.getBoolean("feather_farmlands", this.featherFarmlands);
			this.silkSlabs = toml.getBoolean("silk_slabs", this.silkSlabs);
			this.flintAndSword = toml.getBoolean("flint_and_sword", this.flintAndSword);
			this.bedrockImpaling = toml.getBoolean("bedrock_impaling", this.bedrockImpaling);
			this.voidLoyalty = toml.getBoolean("void_loyalty", this.voidLoyalty);
			this.fullMending = toml.getBoolean("full_mending", this.fullMending);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.save();
	}
}
