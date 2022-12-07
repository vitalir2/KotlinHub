import com.github.jengelman.gradle.plugins.shadow.ShadowExtension

plugins {
    `java` // for 'shadowJar' task
    kotlin("multiplatform") version "1.7.21"
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.vitalir"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            project.extensions.configure<ShadowExtension> {
                component(this@create)
            }
        }
    }
    repositories {
        maven {
            // "platform" prefix because shared already taken by server-shared =/
            val homeDir = System.getProperty("user.home")
            url = uri("file://$homeDir/.m2/libs/kotlinhub")
        }
    }
}
