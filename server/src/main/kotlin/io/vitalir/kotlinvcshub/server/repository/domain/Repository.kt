package io.vitalir.kotlinvcshub.server.repository.domain

import io.vitalir.kotlinvcshub.server.user.domain.model.User
import java.time.LocalDateTime

data class Repository(
    val owner: User,
    val name: String,
    val accessMode: AccessMode,
    val createdAt: LocalDateTime,
    val lastUpdated: LocalDateTime,
    val description: String? = null,
    val content: Content? = null,
    val commits: List<Commit> = emptyList(),
) {

    enum class AccessMode {
        PUBLIC,
        PRIVATE,
    }

    data class Content(
        val rootNodes: List<Node>,
    ) {

        sealed class Node(
            open val name: String,
            open val path: String,
        ) {

            data class File(
                override val name: String,
                override val path: String,
                val content: String,
            ) : Node(name, path)

            data class Directory(
                override val name: String,
                override val path: String,
                val nodes: List<Node>,
            ) : Node(name, path)
        }
    }

    data class Commit(
        val author: User,
        val message: String,
    )
}
