package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.shared.common.network.Url

interface CreateRepositoryUseCase {

    suspend operator fun invoke(initData: CreateRepositoryData): CreateRepositoryResult
}

typealias CreateRepositoryResult = Either<RepositoryError.Create, Url>
