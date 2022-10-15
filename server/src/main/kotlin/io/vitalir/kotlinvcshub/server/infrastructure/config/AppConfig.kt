package io.vitalir.kotlinvcshub.server.infrastructure.config

data class AppConfig(
    val jwtConfig: Jwt,
) {

    data class Jwt(
        val secret: String,
        val issuer: String,
        val audience: String,
        val realm: String,
    )
}
