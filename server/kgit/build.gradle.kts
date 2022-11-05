plugins {
    kotlin("jvm") version "1.7.20"
}

group = "io.vitalir.kotlingit"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Jetty; version < 11 because org.eclipse.jgit.http.server does not support Jakarta
    implementation("org.eclipse.jetty:jetty-server:10.0.12")
    implementation("org.eclipse.jetty:jetty-servlet:10.0.12")

    // Servlet
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.eclipse.jgit:org.eclipse.jgit.http.server:6.3.0.202209071007-r")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.3.2")
}
