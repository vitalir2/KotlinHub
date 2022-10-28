package io.vitalir.kotlinvschub.server.repository.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.vitalir.kotlinvcshub.server.repository.domain.CreateRepositoryData
import io.vitalir.kotlinvcshub.server.repository.domain.Repository
import io.vitalir.kotlinvcshub.server.repository.domain.RepositoryError
import io.vitalir.kotlinvcshub.server.repository.domain.usecases.CreateRepositoryUseCase
import io.vitalir.kotlinvcshub.server.repository.domain.usecases.impl.CreateRepositoryUseCaseImpl

class CreateRepositoryUseCaseSpec : ShouldSpec() {

    private lateinit var createRepositoryUseCase: CreateRepositoryUseCase

    init {
        beforeEach {
            createRepositoryUseCase = CreateRepositoryUseCaseImpl()
        }

        should("return success if data is valid") {
            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    userId = 123,
                    name = "any",
                    accessMode = Repository.AccessMode.PUBLIC,
                )
            )

            result shouldBeRight Unit
        }

        should("return error if user does not exist") {
            val notExistingUserId = 5
            val result = createRepositoryUseCase(
                CreateRepositoryData(
                    userId = notExistingUserId,
                    name = "any",
                    accessMode = Repository.AccessMode.PUBLIC,
                )
            )

            result shouldBeLeft RepositoryError.Create.InvalidUserId
        }
    }
}
