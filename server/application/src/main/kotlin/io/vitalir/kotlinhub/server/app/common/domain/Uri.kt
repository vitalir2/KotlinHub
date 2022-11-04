package io.vitalir.kotlinhub.server.app.common.domain

@JvmInline
value class Uri private constructor(val value: String) {

    enum class Scheme(val stringValue: String) {
        GIT("git"),
    }

    companion object {
        const val HOST = "localhost:8080"

        fun create(
            scheme: Scheme,
            vararg pathParts: String,
        ): Uri {
            val uriValue = buildString {
                append(scheme.stringValue, "://", HOST)
                for (pathPart in pathParts) {
                    append("/", pathPart)
                }
            }
            return Uri(uriValue)
        }
    }
}
