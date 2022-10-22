package io.vitalir.kotlinvcshub.server.infrastructure.config

data class AppConfig(
    val jwt: Jwt,
    val database: Database,
) {

    data class Jwt(
        val secret: String,
        val issuer: String,
        val audience: String,
        val realm: String,
    )

    data class Database(
        val username: String,
        val password: String,
        val databaseName: String,
        val serverName: String,
    )
}
