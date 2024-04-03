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
# Install
```kotlin
dependencies {
    implementation("io.paoloconte:kotlin-itsok:1.0")
}
```
