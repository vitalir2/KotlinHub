package io.vitalir.web.pages.main.models

import io.vitalir.kotlinhub.shared.feature.user.UserId

data class User(
    val userId: UserId,
    val name: String,
    val description: String?,
    val imageUrl: String?,
)
