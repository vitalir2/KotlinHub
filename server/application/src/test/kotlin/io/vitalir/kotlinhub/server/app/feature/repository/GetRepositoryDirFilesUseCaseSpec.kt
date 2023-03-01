package io.vitalir.kotlinhub.server.app.feature.repository

import io.kotest.core.spec.style.ShouldSpec
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryDirFilesUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl.GetRepositoryDirFilesUseCaseImpl

class GetRepositoryDirFilesUseCaseSpec : ShouldSpec() {

    private lateinit var useCase: GetRepositoryDirFilesUseCase

    init {
        beforeEach {
            useCase = GetRepositoryDirFilesUseCaseImpl()
        }
    }
}
