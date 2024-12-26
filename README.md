![badge][badge-android]
![badge][badge-jvm]
![badge][badge-js]
![badge][badge-nodejs]
![badge][badge-linux]
![badge][badge-windows]
![badge][badge-wasm]
![badge][badge-ios]
![badge][badge-mac]
![badge][badge-tvos]
![badge][badge-watchos]
![badge][badge-js-ir]
![badge][badge-apple-silicon]

# ItsOk
This is a little library that provides an improved way to use `Result` in Kotlin, with custom error types and also a way to **avoid wrapping** result objects into Ok and Error wrapper objects.

### Why
There are two cool ways, in Kotlin standard library, to handle errors: `kotlin.Result` and sealed classes; though both present some limitations.  
The standard `Result` uses exceptions as error type, which means you can't specify the type of possible error values.   
With sealed classes you can define the set of both errors and ok values, but you lose the general flexibility and expressiveness of `Result`.
### This library
This library aims to provide the best of both worlds, by providing a `Result` type that can be used with any type of error, 
and a set of extension functions to make it easier to work with it.  
Furthermore, it provides two additional interfaces, `ItsOk` and `ItsError` which allow you to avoid wrapping results into `Ok` and `Error` objects, for cleaner and more efficient code.  
Both the error and ok types can be a single class or multiple classes that all inherit from `ItsOk` and `ItsError`, especially useful to define in a sealed interface or sealed class.   
See the example below to understand how it works.

## Example
```kotlin
import io.paoloconte.itsok.*

sealed interface RepositoryError: ItsError<RepositoryError> {
    object NotFound : RepositoryError
    data class InvalidInput(val message: String) : RepositoryError
}

data class User(val name: String, val age: Int): ItsOk<User>

fun findUser(id: Int): Result<User, RepositoryError> {
    return if (id == 1)
        User("Mario", 30)           // no need to wrap in Ok
    else
        RepositoryError.NotFound    // no need to wrap in Error
}

fun main() {
    val result = findUser(1)
        .map { user ->
            user.name
        }
        .getOrElse {
            println("User not found")
            return
        }

    println("User name is -> $result")
}
```
# Methods
```kotlin
fun <T, E> Result<T, E>.isOk(): Boolean 

fun <T, E> Result<T, E>.isError(): Boolean 

fun <T, E> Result<T, E>.getOrNull(): T? 

fun <T, E> Result<T, E>.getErrorOrNull(): E?

fun <T, E> Result<T, E>.getErrorOrThrow(): E

fun <T, E, R> Result<T, E>.map(transform: (T) -> R): Result<R, E>

fun <T, E, R> Result<T, E>.mapError(transform: (E) -> R): Result<T, R>

fun <T, E> Result<T, E>.recover(transform: (E) -> T): Result<T, E>

fun <T, E> Result<T, E>.onSuccess(block: Ok<T>.(T) -> Unit): Result<T, E> 

fun <T, E> Result<T, E>.onError(block: Error<E>.(E) -> Unit): Result<T, E> 

fun <T, E> Result<T, E>.getOrElse(onFailure: (E) -> T): T

fun <T, E> Result<T, E>.getOrDefault(defaultValue: T): T

fun <T, E, R> Result<T, E>.fold(onSuccess: (T) -> R, onError: (E) -> R): R

fun <T, E, R> Result<T, E>.flatMap(transform: Result<T, E>.(T) -> Result<R, E>): Result<R, E>

fun <T, E, R> Result<T, E>.flatMapError(transform: Result<T, E>.(E) -> Result<T, R>): Result<T, R>

fun <T, E, R> Result<T, E>.andThen(transform: Result<T, E>.(T) -> Result<R, E>): Result<R, E>  // same as flatMap

fun <T, E, F> Result<T, E>.orElse(onFailure: (E) -> Result<T, F>): Result<T, F>  // same as flatMapError

fun <T> resultCatching(block: () -> T): Result<T, Throwable> 

suspend fun <T> suspendCatching(block: suspend () -> T): Result<T, Throwable>

fun <T, E> Result<T, E>.getOrThrow(): T

fun <E> Result<Unit, E>.and(other: Result<Unit, E>): Result<Unit, E>

fun <T, E> Result<T, E>.or(other: Result<T, E>): Result<T, E>
```

# Install
```kotlin
dependencies {
    implementation("io.paoloconte:kotlin-itsok:1.0.9")
}
```

[badge-android]: http://img.shields.io/badge/-android-6EDB8D.svg?style=flat
[badge-android-native]: http://img.shields.io/badge/support-[AndroidNative]-6EDB8D.svg?style=flat
[badge-jvm]: http://img.shields.io/badge/-jvm-DB413D.svg?style=flat
[badge-js]: http://img.shields.io/badge/-js-F8DB5D.svg?style=flat
[badge-js-ir]: https://img.shields.io/badge/support-[IR]-AAC4E0.svg?style=flat
[badge-nodejs]: https://img.shields.io/badge/-nodejs-68a063.svg?style=flat
[badge-linux]: http://img.shields.io/badge/-linux-2D3F6C.svg?style=flat
[badge-windows]: http://img.shields.io/badge/-windows-4D76CD.svg?style=flat
[badge-wasm]: https://img.shields.io/badge/-wasm-624FE8.svg?style=flat
[badge-apple-silicon]: http://img.shields.io/badge/support-[AppleSilicon]-43BBFF.svg?style=flat
[badge-ios]: http://img.shields.io/badge/-ios-CDCDCD.svg?style=flat
[badge-mac]: http://img.shields.io/badge/-macos-111111.svg?style=flat
[badge-watchos]: http://img.shields.io/badge/-watchos-C0C0C0.svg?style=flat
[badge-tvos]: http://img.shields.io/badge/-tvos-808080.svg?style=flat