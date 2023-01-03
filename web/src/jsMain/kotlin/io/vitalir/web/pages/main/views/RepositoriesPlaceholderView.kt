package io.vitalir.web.pages.main.views

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
internal fun RepositoriesPlaceholderView() {
    H2 {
        Text("No repositories yet")
    }
    P {
        A(href = null) {
            Text("Add")
        }
        Text(" a new one or ")
        A(href = null) {
            Text("watch")
        }
        Text(" other repositories")
    }
}
