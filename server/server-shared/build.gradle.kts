val kotlinVersion = "1.7.20"

plugins {
    val kotlinVersion = "1.7.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

repositories {
    mavenCentral()
}

dependencies {
    api("io.vitalir:platform-shared")
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}

group = "io.vitalir"
version = "1.0"
