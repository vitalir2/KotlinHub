package io.vitalir.web.common

sealed interface Loadable<out T> {

    val valueOrNull: T?
        get() = if (this is Loaded) value else null

    object Loading : Loadable<Nothing>
    data class Loaded<T>(val value: T) : Loadable<T>
    data class Error(val exception: Exception) : Loadable<Nothing>

}
