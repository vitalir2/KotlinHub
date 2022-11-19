package io.vitalir.kotlinhub.server.app.feature.user

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beOfType
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

class UpdateUserUseCaseSpec : ShouldSpec() {

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

        should("return error if new login is not valid") {
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

            result.shouldBeLeft()
            result.value should beOfType<UpdateUserUseCase.Error.InvalidArguments>()
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

            result.shouldBeLeft()
            result.value should beOfType<UpdateUserUseCase.Error.InvalidArguments>()
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

            result.shouldBeLeft()
            result.value should beOfType<UpdateUserUseCase.Error.NoUser>()
            result.value shouldBe UpdateUserUseCase.Error.NoUser(userId = notExistingUserId)
        }

        should("return success if user exists and username to change is valid") {
            coEvery {
                userPersistence.getUser(someUserIdentifier)
            } returns User.any
            coEvery {
                userIdentifierValidationRule.validate(UserIdentifier.Username(someUsername))
            } returns Unit.right()

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

            result.shouldBeLeft()
            result.value should beOfType<UpdateUserUseCase.Error.InvalidArguments>()
        }
    }
}

private val <T> T.asNewValue: UpdateUserUseCase.UpdateData.Value.New<T>
    get() = UpdateUserUseCase.UpdateData.Value.New(this)
