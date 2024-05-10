package io.paoloconte.itsok

actual fun Throwable.isCancellation(): Boolean {
    return this::class.java.name == "kotlinx.coroutines.CancellationException"
}