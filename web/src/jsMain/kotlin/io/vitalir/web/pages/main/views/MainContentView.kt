package io.vitalir.web.pages.main.views

import androidx.compose.runtime.Composable
import io.vitalir.web.pages.main.models.Repository
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.dom.Div

@Composable
internal fun RepositoriesContentView(
    repositories: List<Repository>,
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                padding(1.cssRem)
            }
        }
    ) {
        if (repositories.isEmpty()) {
            RepositoriesPlaceholderView()
        } else {
            RepositoriesView(repositories)
        }
    }
}
