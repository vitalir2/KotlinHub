package io.vitalir.server.kgit.git

import org.eclipse.jgit.http.server.GitSmartHttpTools

enum class GitService {
    UPLOAD,
    RECEIVE,
    ;

    companion object {
        fun fromString(value: String): GitService? = when (value) {
            GitSmartHttpTools.RECEIVE_PACK -> RECEIVE
            GitSmartHttpTools.UPLOAD_PACK -> UPLOAD
            else -> null
        }
    }
}
