package io.vitalir.web

import io.vitalir.web.pages.main.MainPage
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        MainPage()
    }
}
