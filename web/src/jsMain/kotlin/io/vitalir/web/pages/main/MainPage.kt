package io.vitalir.web.pages.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.vitalir.web.common.Loadable
import io.vitalir.web.pages.main.views.RepositoriesContentView
import io.vitalir.web.pages.main.views.ProfileView
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun MainPage(controller: MainController) {
    val state by controller.state.collectAsState()

    LaunchedEffect(Any()) {
        controller.refreshRepositories()
    }

    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                justifyContent(JustifyContent.Center)
                gap(0.5.cssRem)
            }
        }
    ) {
        ProfileView(state.user.valueOrNull!!) // TODO
        VerticalSeparator()

        when (val repositories = state.repositories) {
            is Loadable.Error -> {
                RepositoryErrorPlaceholderView(repositories.exception)
            }
            is Loadable.Loaded -> {
                RepositoriesContentView(repositories.value)

            }
            is Loadable.Loading -> {
                LoadingRepositoriesView()
            }
        }
    }
}

@Composable
fun LoadingRepositoriesView() {
    Text("Progress") // TODO
}

@Composable
fun RepositoryErrorPlaceholderView(exception: Exception) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
            }
        }
    ) {
        H3 {
            Text(exception.message.orEmpty())
        }
    }
}

@Composable
private fun VerticalSeparator() {
    Div(
        attrs = {
            style {
                property("border-right-width", "1px")
            }
        }
    )
}
