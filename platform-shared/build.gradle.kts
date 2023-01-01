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
        val ktorVersion = "2.2.1"

        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
            }
        }
    }
}
