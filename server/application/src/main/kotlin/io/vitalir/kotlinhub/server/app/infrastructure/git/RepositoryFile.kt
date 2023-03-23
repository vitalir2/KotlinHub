package io.vitalir.kotlinhub.server.app.infrastructure.git

data class RepositoryFile(
    val name: String,
    val type: Type,
) {
    enum class Type {
        FOLDER,
        REGULAR,
        UNKNOWN,
    }
}
