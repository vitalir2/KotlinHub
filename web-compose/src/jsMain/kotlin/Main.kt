import io.vitalir.web.main.MainPage
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        MainPage()
    }
}
