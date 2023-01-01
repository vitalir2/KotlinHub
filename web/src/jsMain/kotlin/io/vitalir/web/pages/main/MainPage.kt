package io.vitalir.web.pages.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import io.vitalir.web.pages.main.controllers.RepositoryController
import io.vitalir.web.pages.main.models.User
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
fun MainPage(repositoryController: RepositoryController) {
    val defaultUser = remember {
        User(
            userId = 1,
            name = "Vitalir",
            description = "Description about me",
            imageUrl = "https://avatars.githubusercontent.com/u/35116812?v=4",
        )
    }

    val repositoriesState = repositoryController.state.collectAsState()

    LaunchedEffect(Any()) {
        repositoryController.refreshRepositories(defaultUser.userId)
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
        ProfileView(defaultUser)
        VerticalSeparator()

        when (val state = repositoriesState.value) {
            is RepositoryController.State.Error -> {
                RepositoryErrorPlaceholderView(state)
            }
            is RepositoryController.State.Loaded -> {
                RepositoriesContentView(state.repositories)

            }
            is RepositoryController.State.Loading -> {
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
fun RepositoryErrorPlaceholderView(state: RepositoryController.State.Error) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
            }
        }
    ) {
        H3 {
            Text(state.text)
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
