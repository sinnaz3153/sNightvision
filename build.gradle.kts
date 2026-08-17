plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.1.0"
    id("com.gradleup.shadow") version "9.3.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.2.1")
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
}


java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("26.2")
        jvmArgs("-Xms2G", "-Xmx2G", "-Dcom.mojang.eula.agree=true")
    }

    processResources {
        val props = mapOf("version" to version )
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
    shadowJar {
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        dependencies {
            // Only merge bStats into the final jar, no other dependencies
            exclude { it.moduleGroup != "org.bstats" }
        }

        // Relocate bStats into the plugin's package to avoid conflicts with other
        // plugins using bStats
        relocate("org.bstats", project.group.toString())
    }
}
