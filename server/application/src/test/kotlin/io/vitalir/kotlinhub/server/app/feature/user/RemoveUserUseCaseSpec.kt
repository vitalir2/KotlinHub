package io.vitalir.kotlinhub.server.app.feature.user

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.mockk
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RemoveUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.RemoveUserUseCaseImpl

internal class RemoveUserUseCaseSpec : ShouldSpec() {

    private lateinit var userPersistence: UserPersistence

    private lateinit var removeUserUseCase: RemoveUserUseCase

    init {
        beforeEach {
            userPersistence = mockk()
            removeUserUseCase = RemoveUserUseCaseImpl(
                userPersistence = userPersistence,
            )
        }

        should("return error if user does not exist") {
            val notExistingUserId = 1
            coEvery {
                userPersistence.isUserExists(UserIdentifier.Id(notExistingUserId))
            } returns false

            val result = removeUserUseCase(notExistingUserId)

            result shouldBeLeft RemoveUserUseCase.Error.UserDoesNotExist(notExistingUserId)
        }

        should("return error if removing of the user is failed") {
            val existingUserId = 12
            coEvery {
                userPersistence.isUserExists(UserIdentifier.Id(existingUserId))
            } returns true
            coEvery {
                userPersistence.removeUser(existingUserId)
            } returns false

            val result = removeUserUseCase(existingUserId)

            result shouldBeLeft RemoveUserUseCase.Error.RemoveFailed
        }

        should("return success if user exists") {
            val existingUserId = 12
            coEvery {
                userPersistence.isUserExists(UserIdentifier.Id(existingUserId))
            } returns true
            coEvery {
                userPersistence.removeUser(existingUserId)
            } returns true

            val result = removeUserUseCase(existingUserId)

            result shouldBeRight Unit
        }
    }
}
