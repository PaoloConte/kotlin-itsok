@file:OptIn(ExperimentalContracts::class)

package io.paoloconte.itsok.kotest

import io.kotest.matchers.types.shouldBeInstanceOf
import io.paoloconte.itsok.ItsError
import io.paoloconte.itsok.ItsOk
import io.paoloconte.itsok.Result
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

inline fun <reified T : ItsOk<T>, reified E> Result<T, E>.shouldBeOk(): T {
    contract {
        returns() implies (this@shouldBeOk is T)
    }
    this.shouldBeInstanceOf<T>()
    return this
}

inline fun <reified T, reified E> Result<T, E>.shouldBeOk(): Result.Ok<T> {
    contract {
        returns() implies (this@shouldBeOk is Result.Ok<T>)
    }
    this.shouldBeInstanceOf<Result.Ok<T>>()
    return this
}

inline fun <reified T, reified E: ItsError<E>> Result<T, E>.shouldBeError(): E {
    contract {
        returns() implies (this@shouldBeError is E)
    }
    this.shouldBeInstanceOf<E>()
    return this
}

inline fun <reified T, reified E> Result<T, E>.shouldBeError(): Result.Error<E> {
    contract {
        returns() implies (this@shouldBeError is Result.Error<E>)
    }
    this.shouldBeInstanceOf<Result.Error<E>>()
    return this
}