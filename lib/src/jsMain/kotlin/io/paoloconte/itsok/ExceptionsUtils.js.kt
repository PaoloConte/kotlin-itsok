package io.paoloconte.itsok

actual fun Throwable.isCancellation(): Boolean {
    return this::class.simpleName == "CancellationException"
}