package io.paoloconte.itsok

internal actual fun Throwable.isCancellation(): Boolean {
    return this::class.qualifiedName == "kotlinx.coroutines.CancellationException"
}