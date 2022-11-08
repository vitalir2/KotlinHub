package io.vitalir.kotlinhub.server.app.repository.domain.usecase

import io.vitalir.kotlinhub.server.app.repository.domain.model.Repository

// TODO handle errors
interface GetRepositoryUseCase {

    suspend operator fun invoke(userName: String, repositoryName: String): Repository
}
