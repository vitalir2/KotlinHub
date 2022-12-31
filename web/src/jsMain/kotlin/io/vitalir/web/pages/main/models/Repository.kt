package io.vitalir.web.pages.main.models

data class Repository(
    val name: String,
    val accessMode: String,
    val description: String?,
    val updatedAt: String,
)
