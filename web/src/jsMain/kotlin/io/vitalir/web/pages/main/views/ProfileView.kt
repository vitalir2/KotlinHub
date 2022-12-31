package io.vitalir.web.pages.main.views

import androidx.compose.runtime.Composable
import io.vitalir.web.pages.main.models.User
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.lineHeight
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
internal fun ProfileView(
    user: User,
) {
    Aside(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(0.5.cssRem)
                padding(0.5.cssRem)
            }
        }
    ) {
        if (user.imageUrl != null) {
            Avatar(user.imageUrl)
        } else {
            AvatarPlaceholder()
        }

        UserName(user.name)

        if (user.description != null) {
            UserDescription(user.description)
        }
    }
}

@Composable
private fun UserDescription(value: String) {
    P(
        attrs = {
            style {
                fontSize(1.cssRem)
                lineHeight(1.5.cssRem)
                fontWeight(300)
                textAlign("start")
            }
        }
    ) {
        Text(value)
    }
}

@Composable
private fun UserName(value: String) {
    P(
        attrs = {
            style {
                fontWeight(700)
                textAlign("start")
            }
        }
    ) {
        Text(value)
    }
}

@Composable
private fun Avatar(imageUrl: String) {
    Img(
        src = imageUrl,
        attrs = {
            style {
                width(12.cssRem)
                height(12.cssRem)
                borderRadius(9999.px)
            }
        }
    )
}

@Composable
private fun AvatarPlaceholder() {
    TODO("Placeholder")
}
