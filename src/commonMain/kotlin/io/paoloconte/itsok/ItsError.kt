package io.paoloconte.itsok

/**
 * Implemented by [Result.Error] instances. The generic type [E] must be the same as the implementing class itself.
 */
@Suppress("UNCHECKED_CAST")
interface ItsError<E: Result.Error<E>> : Result.Error<E> {
    override val error: E get() = this as E
}
