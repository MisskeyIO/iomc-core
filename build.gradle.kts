import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.*

/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    kotlin("jvm") version "1.8.0"

    `maven-publish`

    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "2.0.4"
}

repositories {
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    library("com.google.code.gson", "gson", "2.8.7")
    bukkitLibrary("com.google.code.gson", "gson", "2.8.7")
}

group = "io.misskey.mc.timeattacker"
version = "4.0.0"
description = "Timeattack Plugin"
java.sourceCompatibility = JavaVersion.VERSION_17

bukkit {
    name = "Timeattacker"
    main = "io.misskey.mc.timeattacker.TimeattackerPlugin"
    version = getVersion().toString()
    apiVersion = "1.19"

    commands {
        register("counter") {
            description = "カウンター管理"
            usage = "/counter <register/unregister/cancel/bind/info/list/resetdaily>"
            permission = "timeattacker.command.counter"
        }
        register("ranking") {
            description = "ランキング管理"
            usage = "/ranking <create/delete/query/list/set/unset/hologram>"
            permission = "timeattacker.command.ranking"
        }
        register("xdebug") {
            description = "timeattacker Debug Command"
            usage = "/xdebug"
            permission = "timeattacker.command.xdebug"
        }
        register("__core_gui_event__") {
            description = "?"
            usage = "?"
        }
    }

    permissions {
        register("timeattacker.command.counter") {
            default = Default.OP
        }
        register("timeattacker.command.ranking") {
            default = Default.OP
        }
        register("timeattacker.command.xdebug") {
            default = Default.OP
        }
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        incremental = true
    }
}

tasks.jar {
    archiveFileName.set("${project.name}.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
