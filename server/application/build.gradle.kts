val ktorVersion = "2.1.2"
val kotlinVersion = "1.7.20"
val logbackVersion = "1.4.4"

plugins {
    val kotlinVersion = "1.7.20"
    application
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("io.ktor.plugin") version "2.1.2"
    id("app.cash.sqldelight") version "2.0.0-alpha04"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.vitalir:server-shared")

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-webjars-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")

    // Arrow Kt
    implementation(platform("io.arrow-kt:arrow-stack:1.1.2"))
    implementation("io.arrow-kt:arrow-core")

    // YAML Config
    implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")

    // Database
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.5.0")
    implementation("app.cash.sqldelight:jdbc-driver:2.0.0-alpha04")

    // Git
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.3.0.202209071007-r")

    // Security
    implementation("org.mindrot:jbcrypt:0.4")

    // OpenAPI
    implementation("io.bkbn:kompendium-core:3.9.0")

    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")

    // Testing
    val kotestVersion = "5.5.1"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.2.5")

    testImplementation("io.mockk:mockk:1.13.2")
}

group = "io.vitalir"
version = "0.0.1"

application {
    mainClass.set("io.vitalir.kotlinhub.server.app.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sqldelight {
    database("MainSqlDelight") {
        packageName = "io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight"
        dialect("app.cash.sqldelight:postgresql-dialect:2.0.0-alpha04")
    }
}

// Kotlin JVM Kotest dependency
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
