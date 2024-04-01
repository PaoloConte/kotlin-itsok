package io.paoloconte.itsok

import io.paoloconte.itsok.Result.Error
import io.paoloconte.itsok.Result.Ok


class BaseOk<out T>(override val value: T): Ok<T>
class BaseError<out E>(override val error: E): Error<E>

/**
 * Creates an instance of [Ok] with the given [value] unless value is already of type [ItsOk].
 */
@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
inline fun <T> Ok(value: T): Ok<T> = if (value is ItsOk<*>) value as Ok<T> else BaseOk(value)

/**
 * Creates an instance of [Error] with the given [error] unless error is already of type [ItsError].
 */
@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
inline fun <E> Error(error: E): Error<E> = if (error is ItsError<*>) error as Error<E> else BaseError(error)

/**
 * Default instance for [Ok] with [Unit] as the value.
 */
val Ok: Ok<Unit> = BaseOk(Unit)
