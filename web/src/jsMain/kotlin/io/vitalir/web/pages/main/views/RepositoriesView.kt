package io.vitalir.web.pages.main.views

import androidx.compose.runtime.Composable
import io.vitalir.web.pages.main.models.Repository
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.borderWidth
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.lineHeight
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
internal fun RepositoriesView(
    repositories: List<Repository>,
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(1.cssRem)
            }
        }
    ) {

    }
    for (repository in repositories) {
        RepositoryView(repository)
    }
}

@Composable
private fun RepositoryView(
    repository: Repository,
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
            }
        }
    ) {
        MainRepositoryInfo(repository)

        if (repository.description != null) {
            RepositoryDescription(repository.description)
        }

        RepositoryUpdatedAt(repository.updatedAt)
    }
}

@Composable
private fun MainRepositoryInfo(repository: Repository) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                gap(0.25.cssRem)
            }
        }
    ) {
        RepositoryName(repository.name)
        RepositoryAccessMode(repository.accessMode)
    }
}

@Composable
private fun RepositoryUpdatedAt(updatedAt: String) {
    P(
        attrs = {
            style {
                fontSize(0.75.cssRem)
                lineHeight(1.cssRem)
                fontWeight(300)
                textAlign("start")
                marginTop(0.25.cssRem)
            }
        }
    ) {
        Text("updated ${updatedAt}")
    }
}

@Composable
private fun RepositoryDescription(value: String) {
    P(
        attrs = {
            style {
                fontSize(0.75.cssRem)
                lineHeight(1.cssRem)
                fontWeight(300)
                textAlign("start")
            }
        }
    ) {
        Text(value)
    }
}

@Composable
private fun RepositoryAccessMode(value: String) {
    Span(
        attrs = {
            style {
                borderWidth(2.px)
                property("border-color", rgb(248, 250, 252))
                fontWeight(700)
            }
        }
    ) {
        Text(value)
    }
}

@Composable
private fun RepositoryName(value: String) {
    A(
        href = null,
        attrs = {
            style {
                color(rgb(2, 132, 199))
                fontWeight(600)
                fontSize(1.125.cssRem)
                lineHeight(1.75.cssRem)
            }
        }
    ) {
        Text(value)
    }
}
