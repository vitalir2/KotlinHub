package io.vitalir.kotlinhub.server.app.feature.user.domain.usecase

import arrow.core.Either
import io.vitalir.kotlinhub.shared.feature.user.UserId

interface UpdateUserUseCase {

    suspend operator fun invoke(
        userId: UserId,
        updateData: UpdateData,
    ): UpdateUserResult
    
    data class UpdateData(
        val username: Value<String> = Value.Old,
        val email: Value<String> = Value.Old,
    ) {
        
        val hasNoPotentialUpdates: Boolean
            get() = username is Value.Old && email is Value.Old
        
        sealed interface Value<out T> {
            object Old : Value<Nothing>
            data class New<T>(val value: T) : Value<T>
        }
    }

    sealed interface Error {

        data class NoUser(val userId: UserId) : Error
        data class InvalidArguments(val message: String) : Error
    }
}

typealias UpdateUserResult = Either<UpdateUserUseCase.Error, Unit>
