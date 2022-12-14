package io.vitalir.kotlinhub.server.app.feature.user

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.UpdateUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.impl.UpdateUserUseCaseImpl
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UserValidationRule

internal class UpdateUserUseCaseSpec : ShouldSpec() {

    private lateinit var userPersistence: UserPersistence

    private lateinit var userIdentifierValidationRule: UserValidationRule<UserIdentifier>

    private lateinit var updateUserUseCase: UpdateUserUseCase

    init {
        val someUserId = 123
        val someUserIdentifier = UserIdentifier.Id(someUserId)
        val someUsername = "someusername"
        val someEmail = "google@google.com"

        beforeEach {
            userPersistence = spyk()
            userIdentifierValidationRule = mockk()
            updateUserUseCase = UpdateUserUseCaseImpl(
                userPersistence = userPersistence,
                userValidationRule = userIdentifierValidationRule,
            )
        }

        should("return error if new username is not valid") {
            val invalidUsername = "a"
            coEvery {
                userPersistence.isUserExists(someUserIdentifier)
            } returns true
            coEvery {
                userIdentifierValidationRule.validate(UserIdentifier.Username(invalidUsername))
            } returns UserError.ValidationFailed.left()

            val result = updateUserUseCase(
                userId = someUserId,
                updateData = UpdateUserUseCase.UpdateData(
                    username = invalidUsername.asNewValue,
                ),
            )

            result shouldBeLeft UpdateUserUseCase.Error.InvalidArgument("username" to invalidUsername)
        }

        should("return error if new email is not valid") {
            val invalidEmail = "a"
            coEvery {
                userPersistence.isUserExists(someUserIdentifier)
            } returns true
            coEvery {
                userIdentifierValidationRule.validate(UserIdentifier.Email(invalidEmail))
            } returns UserError.ValidationFailed.left()

            val result = updateUserUseCase(
                userId = someUserId,
                updateData = UpdateUserUseCase.UpdateData(
                    email = invalidEmail.asNewValue,
                ),
            )

            result shouldBeLeft UpdateUserUseCase.Error.InvalidArgument("email" to invalidEmail)
        }

        should("return error if user does not exist") {
            val notExistingUserId = 1
            coEvery {
                userPersistence.isUserExists(UserIdentifier.Id(notExistingUserId))
            } returns false
            coEvery {
                userIdentifierValidationRule.validate(any())
            } returns Unit.right()

            val result = updateUserUseCase(
                userId = notExistingUserId,
                updateData = UpdateUserUseCase.UpdateData(
                    username = someUsername.asNewValue,
                ),
            )

            result shouldBeLeft UpdateUserUseCase.Error.NoUser(userId = notExistingUserId)
        }

        should("return success if user exists and username to change is valid") {
            coEvery {
                userPersistence.getUser(someUserIdentifier)
            } returns User.any
            coEvery {
                userIdentifierValidationRule.validate(UserIdentifier.Username(someUsername))
            } returns Unit.right()
            coEvery {
                userPersistence.isUserExists(UserIdentifier.Username(someUsername))
            } returns false

            val result = updateUserUseCase(
                userId = someUserId,
                updateData = UpdateUserUseCase.UpdateData(
                    username = someUsername.asNewValue,
                ),
            )

            result shouldBeRight Unit
            coVerify {
                userPersistence.updateUsername(
                    userId = someUserId,
                    username = someUsername,
                )
            }
        }

        should("return success if user exists and email to change is valid") {
            coEvery {
                userPersistence.getUser(someUserIdentifier)
            } returns User.any
            coEvery {
                userIdentifierValidationRule.validate(UserIdentifier.Email(someEmail))
            } returns Unit.right()
            coEvery {
                userPersistence.isUserExists(UserIdentifier.Email(someEmail))
            } returns false

            val result = updateUserUseCase(
                userId = someUserId,
                updateData = UpdateUserUseCase.UpdateData(
                    email = someEmail.asNewValue,
                ),
            )

            result shouldBeRight Unit
            coVerify {
                userPersistence.updateEmail(
                    userId = someUserId,
                    email = someEmail,
                )
            }
        }

        should("return error if no data is updating") {
            coEvery {
                userPersistence.isUserExists(someUserIdentifier)
            } returns true

            val result = updateUserUseCase(userId = someUserId, UpdateUserUseCase.UpdateData())

            result shouldBeLeft UpdateUserUseCase.Error.NoUpdates
        }

        should("return error if user with such updated username exists (conflict)") {
            val existingUsername = "imexistingusername"
            coEvery {
                userPersistence.getUser(someUserIdentifier)
            } returns User.any
            coEvery {
                userPersistence.isUserExists(UserIdentifier.Username(existingUsername))
            } returns true
            coEvery {
                userIdentifierValidationRule.validate(any())
            } returns Unit.right()

            val result = updateUserUseCase(
                userId = someUserId,
                updateData = UpdateUserUseCase.UpdateData(
                    username = existingUsername.asNewValue,
                ),
            )

            result shouldBeLeft UpdateUserUseCase.Error.Conflict("username" to existingUsername)
        }

        should("return error if user with such updated email exists (conflict)") {
            val existingEmail = "imexistingusername"
            coEvery {
                userPersistence.getUser(someUserIdentifier)
            } returns User.any
            coEvery {
                userPersistence.isUserExists(UserIdentifier.Email(existingEmail))
            } returns true
            coEvery {
                userIdentifierValidationRule.validate(any())
            } returns Unit.right()

            val result = updateUserUseCase(
                userId = someUserId,
                updateData = UpdateUserUseCase.UpdateData(
                    email = existingEmail.asNewValue,
                ),
            )

            result shouldBeLeft UpdateUserUseCase.Error.Conflict("email" to existingEmail)
        }
    }
}

private val <T> T.asNewValue: UpdateUserUseCase.UpdateData.Value.New<T>
    get() = UpdateUserUseCase.UpdateData.Value.New(this)
