package io.vitalir.kotlinvcshub.server.repository.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinvcshub.server.common.domain.Uri
import io.vitalir.kotlinvcshub.server.repository.domain.model.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.model.RepositoryError

interface CreateRepositoryUseCase {

    suspend operator fun invoke(initData: CreateRepositoryData): Either<RepositoryError.Create, Uri>
}
