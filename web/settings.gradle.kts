includeBuild("../platform-shared")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version("1.7.20")
        id("org.jetbrains.compose").version("1.2.2")
    }
}

rootProject.name = "web"
