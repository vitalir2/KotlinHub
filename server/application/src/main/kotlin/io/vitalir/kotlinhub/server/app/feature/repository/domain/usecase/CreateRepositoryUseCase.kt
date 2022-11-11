package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.server.app.common.domain.Uri
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError

interface CreateRepositoryUseCase {

    suspend operator fun invoke(initData: CreateRepositoryData): Either<RepositoryError.Create, Uri>
}
