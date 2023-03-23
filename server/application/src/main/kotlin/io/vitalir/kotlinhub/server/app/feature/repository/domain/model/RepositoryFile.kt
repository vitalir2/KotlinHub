package io.vitalir.kotlinhub.server.app.feature.repository.domain.model

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
