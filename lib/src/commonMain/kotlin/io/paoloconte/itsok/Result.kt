
package io.paoloconte.itsok


sealed interface Result<out T, out E> {
    interface Ok<out T>: Result<T, Nothing> {
        val wrappedValue: T
    }
    interface Error<out E>: Result<Nothing, E> {
        val wrappedError: E
    }
}



