package io.paoloconte.itsok

internal actual fun Throwable.isCancellation(): Boolean {
    return this::class.java.name == "kotlinx.coroutines.CancellationException"

}