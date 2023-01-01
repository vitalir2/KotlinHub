package io.vitalir.web

import io.vitalir.web.network.baseHttpClient
import io.vitalir.web.pages.main.MainPage
import io.vitalir.web.pages.main.controllers.RepositoryController
import org.jetbrains.compose.web.renderComposable

fun main() {
    val controller = RepositoryController(baseHttpClient)
    renderComposable(rootElementId = "root") {
        MainPage(controller)
    }
}
