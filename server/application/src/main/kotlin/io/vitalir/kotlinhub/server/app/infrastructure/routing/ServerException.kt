package io.vitalir.kotlinhub.server.app.infrastructure.routing

class ServerException(override val message: String) : Exception(message)
