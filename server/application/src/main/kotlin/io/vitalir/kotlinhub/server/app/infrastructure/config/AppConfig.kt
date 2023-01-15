package io.vitalir.kotlinhub.server.app.infrastructure.config

data class AppConfig(
    val debug: Debug?,
    val auth: Auth,
    val database: Database,
    val repository: Repository,
) {

    val isDevelopmentMode: Boolean
        get() = debug != null

    data class Debug(
        val isRoutesTracingEnabled: Boolean,
        val isDocsEnabled: Boolean,
    )

    data class Auth(
        val jwt: Jwt,
        val defaultAdmin: DefaultAdmin,
    ) {

        data class Jwt(
            val secret: String,
            val issuer: String,
            val audience: String,
            val realm: String,
        )

        data class DefaultAdmin(
            val username: String,
            val password: String,
        )
    }

    data class Database(
        val username: String,
        val password: String,
        val databaseName: String,
        val serverName: String,
    )

    data class Repository(
        val baseRepositoriesPath: String,
    )
}
