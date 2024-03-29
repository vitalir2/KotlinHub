package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.continuations.nullable
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.Repository
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryIdentifier
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.HasUserAccessToRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserCredentials
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.validation.UserValidationRule
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager

internal class HasUserAccessToRepositoryUseCaseImpl(
    private val userPersistence: UserPersistence,
    private val repositoryPersistence: RepositoryPersistence,
    private val passwordManager: PasswordManager,
    private val userIdentifierValidationRule: UserValidationRule<UserIdentifier>,
) : HasUserAccessToRepositoryUseCase {
    override suspend fun invoke(
        userCredentials: UserCredentials,
        repositoryIdentifier: RepositoryIdentifier,
    ): Boolean {
        return nullable {
            if (userIdentifierValidationRule.validate(userCredentials.identifier).isLeft()) {
                return@nullable false
            }

            val repository = repositoryPersistence.getRepository(
                repositoryIdentifier,
            ).bind()
            if (repository.isPublic) {
                return@nullable true
            }

            val user = userPersistence.getUser(userCredentials.identifier).bind()
            val isCredentialsValid = passwordManager.comparePasswords(userCredentials.password, user.password)
            isCredentialsValid
        } ?: false
    }

    override suspend fun invoke(
        userIdentifier: UserIdentifier?,
        repositoryIdentifier: RepositoryIdentifier,
    ): Boolean {
        val repository = repositoryPersistence.getRepository(
            repositoryIdentifier,
        ) ?: return false // TODO return that it does not exist
        return when (repository.accessMode) {
            Repository.AccessMode.PUBLIC -> true
            Repository.AccessMode.PRIVATE -> {
                if (userIdentifier != null) {
                    val user = userPersistence.getUser(userIdentifier)
                    user?.id == repository.owner.id
                } else {
                    false
                }
            }
        }
    }
}
