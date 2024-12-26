@file:Suppress("NOTHING_TO_INLINE")

package io.paoloconte.itsok

import io.paoloconte.itsok.Result.Error
import io.paoloconte.itsok.Result.Ok
import kotlin.contracts.contract
import kotlin.jvm.JvmName

@JvmName("isItsOk")
inline fun <reified T: ItsOk<T>, E> Result<T, E>.isOk(): Boolean {
    contract {
        returns(true) implies (this@isOk is T)
        returns(false) implies (this@isOk is Error<E>)
    }
    return this is Ok
}

inline fun <T, E> Result<T, E>.isOk(): Boolean {
    contract {
        returns(true) implies (this@isOk is Ok<T>)
        returns(false) implies (this@isOk is Error<E>)
    }
    return this is Ok
}

@JvmName("isItsError")
inline fun <T, reified E: ItsError<E>> Result<T, E>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is E)
        returns(false) implies (this@isError is Ok<T>)
    }
    return this is Error
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
        is Ok -> wrappedValue
        else -> null
    }

inline fun <T, E> Result<T, E>.getErrorOrNull(): E? =
    when (this) {
        is Error -> wrappedError
        else -> null
    }

inline fun <T, E> Result<T, E>.getErrorOrThrow(): E =
    when (this) {
        is Error -> wrappedError
        else -> error("Result is not an Error")
    }

inline fun <T, E, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> =
    when (this) {
        is Ok -> Ok(transform(wrappedValue))
        is Error -> this
    }

inline fun <T, E, R> Result<T, E>.mapError(transform: (E) -> R): Result<T, R> =
    when (this) {
        is Ok -> this
        is Error -> Error(transform(wrappedError))
    }

inline fun <T, E> Result<T, E>.recover(transform: (E) -> T): Result<T, E> =
    when (this) {
        is Ok -> this
        is Error -> Ok(transform(wrappedError))
    }

inline fun <T, E> Result<T, E>.onSuccess(block: Ok<T>.(T) -> Unit): Result<T, E> {
    if (this is Ok) block(wrappedValue)
    return this
}

inline fun <T, E> Result<T, E>.onError(block: Error<E>.(E) -> Unit): Result<T, E> {
    if (this is Error) block(wrappedError)
    return this
}

inline fun <T, E> Result<T, E>.getOrElse(onFailure: (E) -> T): T =
    when (this) {
        is Ok -> wrappedValue
        is Error -> onFailure(wrappedError)
    }

inline fun <T, E> Result<T, E>.getOrDefault(defaultValue: T): T =
    when (this) {
        is Ok -> wrappedValue
        is Error -> defaultValue
    }

inline fun <T, E, R> Result<T, E>.fold(onSuccess: (T) -> R, onError: (E) -> R): R =
    when (this) {
        is Ok -> onSuccess(wrappedValue)
        is Error -> onError(wrappedError)
    }

inline fun <T, E, R> Result<T, E>.flatMap(transform: Result<T, E>.(T) -> Result<R, E>): Result<R, E> =
    when (this) {
        is Ok -> transform(wrappedValue)
        is Error -> this
    }

inline fun <T, E, R> Result<T, E>.flatMapError(transform: Result<T, E>.(E) -> Result<T, R>): Result<T, R> =
    when (this) {
        is Ok -> this
        is Error -> transform(wrappedError)
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
        is Ok -> return wrappedValue
        is Error -> (wrappedError as? Throwable)?.let { throw it } ?: error("Result is Error")
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
