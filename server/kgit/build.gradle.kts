plugins {
    val kotlinVersion = "1.7.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.vitalir.kotlingit"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Jetty; version < 11 because org.eclipse.jgit.http.server does not support Jakarta
    val jettyVersion = "10.0.12"
    implementation("org.eclipse.jetty:jetty-server:$jettyVersion")
    implementation("org.eclipse.jetty:jetty-servlet:$jettyVersion")

    // Servlet
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.eclipse.jgit:org.eclipse.jgit.http.server:6.3.0.202209071007-r")

    // Http Client (Ktor)
    val ktorVersion = "2.1.2"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.3.2")
}

tasks {
    named<Jar>("jar") {
        manifest {
            attributes("Main-Class" to "io.vitalir.server.kgit.MainKt")
        }
    }
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        configurations = listOf(
            project.configurations.runtimeClasspath.get(),
        )
    }
}
