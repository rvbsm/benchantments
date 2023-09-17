val gitVersion: groovy.lang.Closure<String> by extra

plugins {
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.shadow)
	alias(libs.plugins.git)
}

group = "dev.rvbsm"
version = gitVersion()

repositories {}

loom {
	splitEnvironmentSourceSets()

	mods.register("benchantments") {
		sourceSet(sourceSets["main"])
		sourceSet(sourceSets["client"])
	}
}

val shadowImplementation: Configuration by configurations.creating {
	configurations.implementation.get().extendsFrom(this)
}

dependencies {
	minecraft(libs.minecraft)
	mappings("${libs.yarn.mappings.get()}:v2")

	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)

	shadowImplementation(libs.toml4j) {
		exclude(module = "gson")
	}

	modImplementation(libs.fabric.loader)
}

tasks {
	compileJava {
		options.encoding = Charsets.UTF_8.name()
		options.release.set(17)
	}

	processResources {
		inputs.property("version", project.version)
		filesMatching("fabric.mod.json") {
			expand("version" to project.version)
		}
	}

	shadowJar {
		configurations = listOf(shadowImplementation)
		archiveClassifier.set("shadow")
	}

	remapJar {
		dependsOn(shadowJar)
		inputFile.set(shadowJar.get().archiveFile)
	}

	jar {
		from(file("LICENSE"))
	}
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}
