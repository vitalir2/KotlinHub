plugins {
    kotlin("multiplatform") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
}

group = "io.vitalir"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js(IR) {
        moduleName = "platform-shared"

        nodejs()
        useCommonJs()
        binaries.library()
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }

        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val jvmMain by getting
        val jsMain by getting {
            dependencies {
                implementation(npm("@js-joda/core", "5.5.2"))
            }
        }
    }
}
