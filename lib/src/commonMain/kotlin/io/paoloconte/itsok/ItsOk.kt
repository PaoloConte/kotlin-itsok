package io.paoloconte.itsok

/**
 * Implemented by [Result.Ok] instances. The generic type [T] must be the same as the implementing class itself.
 */
@Suppress("UNCHECKED_CAST")
interface ItsOk<T: Result.Ok<T>> : Result.Ok<T> {
    override val wrappedValue: T get() = this as T
}
