package io.vitalir.kotlinhub.server.app.infrastructure.git

sealed interface GitError {
    data class DirectoryNotFound(val dirPath: String) : GitError
    data class FileNotFound(val filePath: String) : GitError
}
