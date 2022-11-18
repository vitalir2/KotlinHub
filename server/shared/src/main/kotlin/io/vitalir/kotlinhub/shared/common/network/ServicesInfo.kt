package io.vitalir.kotlinhub.shared.common.network

// TODO get this info from env to share between docker compose, nginx, etc
object ServicesInfo {

    object App : ServiceInfo {
        override val mainUrl: Url = Url(
            scheme = Scheme.HTTP,
            host = "app",
            port = 8080,
        )
    }

    object Kgit : ServiceInfo {
        override val mainUrl: Url = Url(
            scheme = Scheme.HTTP,
            host = "kgit",
            port = 8081,
        )
    }

    object ReverseProxy : ServiceInfo {
        override val mainUrl: Url = Url(
            scheme = Scheme.HTTP,
            host = "localhost",
        )
    }
}
