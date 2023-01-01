package io.vitalir.web.pages.main

import io.vitalir.web.common.Loadable
import io.vitalir.web.pages.main.models.Repository
import io.vitalir.web.pages.main.models.User

data class MainState(
    val user: Loadable<User> = Loadable.Loaded(
        // TODO
        User(
            userId = 1,
            name = "Vitalir",
            description = "Description about me",
            imageUrl = "https://avatars.githubusercontent.com/u/35116812?v=4",
        )
    ),
    val repositories: Loadable<List<Repository>> = Loadable.Loading,
)
