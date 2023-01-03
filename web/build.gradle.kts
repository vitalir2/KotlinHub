import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "io.vitalir"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser {
            runTask {
                devServer = KotlinWebpackConfig.DevServer(
                    open = true,
                    contentBase = mutableListOf(compilation.output.resourcesDir.canonicalPath) ,
                    port = 8090,
                )
            }
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val ktorVersion = "2.2.1"

        val commonMain by getting {
            dependencies {
                implementation("io.vitalir:platform-shared")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation(compose.web.core)
                implementation(compose.runtime)

                val bootstrapCompose = "0.1.12"
                implementation("app.softwork:bootstrap-compose:$bootstrapCompose")
                implementation("app.softwork:bootstrap-compose-icons:$bootstrapCompose")
            }
        }

        val jsTest by getting {
        }
    }
}
