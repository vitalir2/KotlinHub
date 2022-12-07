val kotlinVersion = "1.7.20"

plugins {
    val kotlinVersion = "1.7.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

repositories {
    mavenCentral()
    val homeDir = System.getProperty("user.home")
    maven {
        url = uri("file://$homeDir/libs/kotlinhub/")
    }
}

dependencies {
    api("io.vitalir:platform-shared:1.0")
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}

group = "io.vitalir"
version = "0.0.1"
