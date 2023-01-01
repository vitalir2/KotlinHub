package io.vitalir.web

import io.vitalir.web.data.RepositoriesRepositoryImpl
import io.vitalir.web.network.baseHttpClient
import io.vitalir.web.pages.main.MainController
import io.vitalir.web.pages.main.MainPage
import org.jetbrains.compose.web.renderComposable

fun main() {
    val repositoriesRepository = RepositoriesRepositoryImpl(baseHttpClient)
    val mainController = MainController(repositoriesRepository)
    renderComposable(rootElementId = "root") {
        MainPage(mainController)
    }
}
