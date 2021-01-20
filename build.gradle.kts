/*
 * IntelliMob, an intelligent plugin to give AI to Java Minecraft Bedrock servers
 * Copyright (C) 2021  José Roberto de Araújo Júnior <joserobjr@powernukkit.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

buildscript {
    val atomicfuVersion = "0.15.1"
    dependencies {
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:$atomicfuVersion")
    }
}

plugins {
    kotlin("multiplatform") version "1.4.21"
}

apply(plugin = "kotlinx-atomicfu")

group = "games.joserobjr"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots") {
        mavenContent { snapshotsOnly() }
    }
    maven("https://repo.opencollab.dev/maven-releases/") {
        mavenContent { releasesOnly() }
    }
    maven("https://repo.opencollab.dev/maven-snapshots/") {
        mavenContent { snapshotsOnly() }
    }
}

val kotlinxCoroutinesVersion = project.findProperty("kotlinx.coroutines.version")

kotlin {
    jvm("powernukkit") {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    jvm("gomint") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    jvm("cloudburst") {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
                implementation(project(":jvm-expectations"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val powernukkitMain by getting {
            dependencies {
                implementation("org.powernukkit:powernukkit:${findProperty("powernukkit.version")}")
            }
        }
        val powernukkitTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
            }
        }
        val gomintMain by getting {
            dependencies {
                val gomintVersion = findProperty("gomint.version")
                implementation("io.gomint:gomint-api:$gomintVersion")
                runtimeOnly("io.gomint:gomint-server:$gomintVersion")
                implementation(kotlin("reflect"))
            }
        }
        val gomintTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
            }
        }
        val cloudburstMain by getting {
            dependencies {
                implementation("org.cloudburstmc:cloudburst-server:${findProperty("cloudburst.server.version")}")
            }
        }
        val cloudburstTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
            }
        }
        all {
            with(languageSettings) {
                enableLanguageFeature("InlineClasses")
                useExperimentalAnnotation("kotlin.RequiresOptIn")
            }
            explicitApi()
        }
    }
}

tasks {
    fun findFileInResources(sourceSetName: String, fileName: String) =
        kotlin.sourceSets.getByName(sourceSetName).resources.srcDirs.asSequence()
            .map { it.resolve(fileName) }
            .first { it.isFile }
    
    fun createSingleFileJar(jarFile: File, entryFile: File) {
        jarFile.outputStream().use { out ->
            ZipOutputStream(out).use { zip ->
                zip.putNextEntry(ZipEntry(entryFile.name))
                entryFile.inputStream().use { input ->
                    input.copyTo(zip)
                }
                zip.closeEntry()
            }
        }
    }
    
    fun mkdirs(file: File) {
        check(file.isDirectory || file.mkdirs()) { "Failed to create $file" }
    }
    
    val preparePowerNukkitRun by creating {
        group = "build setup"
        val pnPlugins = projectDir.resolve("run/powernukkit/plugins")
        val pluginYml = findFileInResources("powernukkitMain", "plugin.yml")
        val outputJar = pnPlugins.resolve("IntelliMob-PowerNukkit-debug.jar")
        
        inputs.file(pluginYml)
        outputs.file(outputJar)
        
        doFirst {
            mkdirs(pnPlugins)
            createSingleFileJar(outputJar, pluginYml)
        }
    }
    
    create(name = "runPowerNukkit", type = JavaExec::class) {
        dependsOn(preparePowerNukkitRun)
        group = "debug"
        main = "cn.nukkit.Nukkit"
        workingDir = projectDir.resolve("run/powernukkit")
        standardInput = System.`in`
        
        
        classpath(objects.fileCollection().from(
            named("compileKotlinPowernukkit"),
            configurations.named("powernukkitRuntimeClasspath")
        ))
    }
    
    val gomintJar = getByName("gomintJar", Jar::class)
    val prepareGoMintRun by creating {
        dependsOn(gomintJar)
        group = "build setup"
        val pluginsDir = projectDir.resolve("run/gomint/plugins")
        val outputJar = pluginsDir.resolve("IntelliMob-GoMint-debug.jar")

        inputs.file(gomintJar.archiveFile)
        outputs.file(outputJar)

        doFirst {
            mkdirs(pluginsDir)
        }
        
        doLast {
            copy {
                from(gomintJar.archiveFile)
                into(pluginsDir)
            }
        }
    }

    create(name = "runGoMint", type = JavaExec::class) {
        dependsOn(prepareGoMintRun)
        group = "debug"
        main = "io.gomint.server.Bootstrap"
        workingDir = projectDir.resolve("run/gomint")
        standardInput = System.`in`
        jvmArgs(
            "-Dio.netty.buffer.checkBounds=false",
            "--add-opens", "java.base/java.nio=io.netty.common",
            "--add-exports", "java.base/jdk.internal.misc=io.netty.common"
        )
        
        classpath(objects.fileCollection().from(
            named("compileKotlinGomint"),
            configurations.named("gomintRuntimeClasspath")
        ))
    }

    val cloudburstJar = getByName("cloudburstJar", Jar::class)
    val prepareCloudburstRun by creating {
        dependsOn(cloudburstJar)
        group = "build setup"
        val pluginsDir = projectDir.resolve("run/cloudburst/plugins")
        //val pluginYml = findFileInResources("cloudburstMain", "plugin.yml")
        val outputJar = pluginsDir.resolve("IntelliMob-Cloudburst-debug.jar")

        //inputs.file(pluginYml)
        inputs.file(cloudburstJar.archiveFile)
        outputs.file(outputJar)

        doFirst {
            mkdirs(pluginsDir)
            //createSingleFileJar(outputJar, pluginYml)
        }

        doLast {
            copy {
                from(gomintJar.archiveFile)
                into(pluginsDir)
            }
        }
    }

    create(name = "runCloudburst", type = JavaExec::class) {
        dependsOn(prepareCloudburstRun)
        group = "debug"
        main = "org.cloudburstmc.server.Bootstrap"
        workingDir = projectDir.resolve("run/cloudburst")
        standardInput = System.`in`


        classpath(objects.fileCollection().from(
            named("compileKotlinCloudburst"),
            configurations.named("cloudburstRuntimeClasspath")
        ))
    }
}
