plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Paper
    compileOnly("io.papermc.paper", "paper-api", "1.20.6-R0.1-SNAPSHOT")

    // Plugins
    compileOnly("com.github.TechFortress", "GriefPrevention", "17.0.0")

    // Command
    paperLibrary("org.incendo", "cloud-paper", "2.0.0-SNAPSHOT")

    // Config
    paperLibrary("org.spongepowered", "configurate-hocon", "4.2.0-SNAPSHOT")
    paperLibrary("net.kyori", "adventure-serializer-configurate4", "4.16.0")

    // Utils
    paperLibrary("com.google.inject", "guice", "7.0.0")
}

paper {
    name = rootProject.name
    version = "1.1.5"
    main = "com.github.namiuni.mobball.MobBall"
    apiVersion = "1.20"
    description = "MOB収納ボールプラグイン"
    author = "Unitarou"
    website = "https://github.com/NamiUni"
    generateLibrariesJson = true

    val mainPackage = "com.github.namiuni.mobball"
    main = "$mainPackage.MobBall"
    bootstrapper = "$mainPackage.MobBallBootstrap"
    loader = "$mainPackage.MobBallPluginLoader"
    serverDependencies {
        register("GriefPrevention") {
            required = false
        }
    }
}

tasks {
    shadowJar {
        this.archiveClassifier.set(null as String?)
        this.archiveVersion.set(paper.version)
    }

    runServer {
        minecraftVersion("1.20.6")
        downloadPlugins {
            //url("https://dev.bukkit.org/projects/grief-prevention/files/4784167/download")
            url("https://download.luckperms.net/1541/bukkit/loader/LuckPerms-Bukkit-5.4.128.jar")
        }
    }

    compileJava {
        this.options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }
}
