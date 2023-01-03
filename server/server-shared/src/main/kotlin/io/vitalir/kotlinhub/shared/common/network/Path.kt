package io.vitalir.kotlinhub.shared.common.network

class Path(private val pathElements: List<String>) {

    constructor(vararg pathElements: String) : this(pathElements.toList())

    override fun toString(): String {
        return pathElements.joinToString("/")
    }
}
