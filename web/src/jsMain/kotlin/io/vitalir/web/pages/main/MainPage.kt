package io.vitalir.web.pages.main

import androidx.compose.runtime.Composable
import io.vitalir.web.pages.main.models.Repository
import io.vitalir.web.pages.main.models.User
import io.vitalir.web.pages.main.views.MainContentView
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

@Composable
fun MainPage() {
    val repositories = listOf(
        Repository(name = "First", accessMode = "Public", description = null, updatedAt = "27.12.2022"),
    )
    val defaultUser = User(
        name = "Vitalir",
        description = "Description about me",
        imageUrl = "https://avatars.githubusercontent.com/u/35116812?v=4",
    )

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
        MainContentView(repositories)
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
