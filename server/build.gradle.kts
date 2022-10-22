val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.20"
    id("io.ktor.plugin") version "2.1.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
    id("app.cash.sqldelight") version "2.0.0-alpha04"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-webjars-jvm:$ktor_version")
    implementation("org.webjars:jquery:3.2.1")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // Arrow Kt
    implementation(platform("io.arrow-kt:arrow-stack:1.1.2"))
    implementation("io.arrow-kt:arrow-core")

    // YAML Config
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")

    // Database
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("app.cash.sqldelight:jdbc-driver:2.0.0-alpha04")
}

group = "io.vitalir"
version = "0.0.1"

application {
    mainClass.set("io.vitalir.kotlinvcshub.server.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sqldelight {
    database("MainSqlDelight") {
        packageName = "io.vitalir.kotlinvcshub.server.infrastructure.database.sqldelight"
        dialect("app.cash.sqldelight:postgresql-dialect:2.0.0-alpha04")
    }
}

// Docker Compose custom tasks
val environments = listOf("dev", "prod")
for (env in environments) {
    val capitalizedEnv = env.capitalize()
    tasks.register("runDockerCompose$capitalizedEnv") {
        dependsOn("buildFatJar")
        doLast {
            exec {
                workingDir = projectDir
                executable = "docker"
                args = listOf("compose", "--env-file", "./config/.env.$env", "up")
            }
        }
    }
    tasks.register("stopDockerCompose$capitalizedEnv") {
        doLast {
            exec {
                workingDir = projectDir
                executable = "docker"
                args = listOf("compose", "--env-file", "./config/.env.$env", "down")
            }
        }
    }
}
