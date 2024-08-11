@file:Suppress("NOTHING_TO_INLINE")

package io.paoloconte.itsok

import io.paoloconte.itsok.Result.Error
import io.paoloconte.itsok.Result.Ok
import kotlin.contracts.contract


inline fun <T, E> Result<T, E>.isOk(): Boolean {
    contract {
        returns(true) implies (this@isOk is Ok<T>)
        returns(false) implies (this@isOk is Error<E>)
    }
    return this is Ok
}

inline fun <T, E> Result<T, E>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is Error<E>)
        returns(false) implies (this@isError is Ok<T>)
    }
    return this is Error
}

inline fun <T, E> Result<T, E>.getOrNull(): T? =
    when (this) {
        is Ok -> value
        else -> null
    }

inline fun <T, E> Result<T, E>.getErrorOrNull(): E? =
    when (this) {
        is Error -> error
        else -> null
    }

inline fun <T, E, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> =
    when (this) {
        is Ok -> Ok(transform(value))
        is Error -> this
    }

inline fun <T, E, R> Result<T, E>.mapError(transform: (E) -> R): Result<T, R> =
    when (this) {
        is Ok -> this
        is Error -> Error(transform(error))
    }

inline fun <T, E, R> Result<T, E>.recover(transform: (E) -> T): Result<T, R> =
    when (this) {
        is Ok -> this
        is Error -> Ok(transform(error))
    }

inline fun <T, E> Result<T, E>.onSuccess(block: Ok<T>.(T) -> Unit): Result<T, E> {
    if (this is Ok) block(value)
    return this
}

inline fun <T, E> Result<T, E>.onError(block: Error<E>.(E) -> Unit): Result<T, E> {
    if (this is Error) block(error)
    return this
}

inline fun <T, E> Result<T, E>.getOrElse(onFailure: (E) -> T): T =
    when (this) {
        is Ok -> value
        is Error -> onFailure(error)
    }

inline fun <T, E> Result<T, E>.getOrDefault(defaultValue: T): T =
    when (this) {
        is Ok -> value
        is Error -> defaultValue
    }

inline fun <T, E, R> Result<T, E>.fold(onSuccess: (T) -> R, onError: (E) -> R): R =
    when (this) {
        is Ok -> onSuccess(value)
        is Error -> onError(error)
    }

inline fun <T, E, R> Result<T, E>.flatMap(transform: Result<T, E>.(T) -> Result<R, E>): Result<R, E> =
    when (this) {
        is Ok -> transform(value)
        is Error -> this
    }

inline fun <T, E, R> Result<T, E>.flatMapError(transform: Result<T, E>.(E) -> Result<T, R>): Result<T, R> =
    when (this) {
        is Ok -> this
        is Error -> transform(error)
    }

inline fun <T, E, R> Result<T, E>.andThen(transform: Result<T, E>.(T) -> Result<R, E>): Result<R, E> = flatMap(transform)

inline fun <T, E, F> Result<T, E>.orElse(onFailure: Result<T, E>.(E) -> Result<T, F>): Result<T, F> = flatMapError(onFailure)

inline fun <T> resultCatching(block: () -> T): Result<T, Throwable> {
    return try {
        Ok(block())
    } catch (t: Throwable) {
        // CancellationException is rethrown to not disrupt coroutine cancellation
        if (t.isCancellation())
            throw t
        Error(t)
    }
}

suspend inline fun <T> suspendCatching(block: suspend () -> T): Result<T, Throwable> {
    return try {
        Ok(block())
    } catch (t: Throwable) {
        // CancellationException is rethrown to not disrupt coroutine cancellation
        if (t.isCancellation())
            throw t
        Error(t)
    }
}

inline fun <T, E> Result<T, E>.getOrThrow(): T {
    contract {
        returns() implies (this@getOrThrow is Ok<T>)
    }
    when (this) {
        is Ok -> return value
        is Error -> (error as? Throwable)?.let { throw it } ?: error("Result is Error")
    }
}

inline fun <E> Result<Unit, E>.and(other: Result<Unit, E>): Result<Unit, E> {
    if (this is Error) return this
    if (other is Error) return other
    return Ok
}

inline fun <T, E> Result<T, E>.or(other: Result<T, E>): Result<T, E> {
    if (this is Ok) return this
    return other
}
