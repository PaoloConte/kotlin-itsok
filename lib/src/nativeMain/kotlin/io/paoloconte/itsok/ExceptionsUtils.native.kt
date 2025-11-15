package io.paoloconte.itsok

actual fun Throwable.isCancellation(): Boolean {
    return this::class.qualifiedName == "kotlinx.coroutines.CancellationException"
}