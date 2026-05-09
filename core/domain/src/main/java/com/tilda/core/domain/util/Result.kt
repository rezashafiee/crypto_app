package com.tilda.core.domain.util

open class DomainError : Exception()

sealed interface Result<out D, out E : DomainError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : DomainError>(val error: E) : Result<Nothing, E>
}

/** Transforms success data while keeping error values unchanged. */
internal inline fun <T, E : DomainError, R> Result<T, E>.map(
    map: (T) -> R
): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

/** Converts any [Result] success payload into [Unit]. */
internal fun <T, E : DomainError> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

/** Runs [action] only when the current result is [Result.Success]. */
inline fun <T, E : DomainError> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

/** Runs [action] only when the current result is [Result.Error]. */
inline fun <T, E : DomainError> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }

        is Result.Success -> this
    }
}

internal typealias EmptyResult<E> = Result<Unit, E>
