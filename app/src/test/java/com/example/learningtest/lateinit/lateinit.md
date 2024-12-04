<!-- TOC -->
* [Lateinit](#lateinit)
  * [Real Use Cases for lateinit in Android Development](#real-use-cases-for-lateinit-in-android-development)
  * [disadvantages of lateinit](#disadvantages-of-lateinit)
  * [Learning Tests](#learning-tests)
<!-- TOC -->

# Lateinit

The `lateinit` is the kotlin modifier that can be used on class properties and top-level
properties.  
It allows for **late initialization** of `var` properties without immediately providing a value.

* It cannot be used with primitive types (Int, Double, etc.).
* The property must be non-nullable (String, not String?).

In Android development, `lateinit` is commonly used for properties that cannot be initialized at the
time of object creation   
but are guaranteed to be initialized before they are used.

## Real Use Cases for lateinit in Android Development

* **View Binding Without Immediate Initialization**: Views like TextView, Button, etc., are typically
  initialized in the onCreate method after `setContentView` is called.
* **Lifecycle-Aware Components**:Components that depend on the Android lifecycle, such as `ViewModel`,
  are initialized in lifecycle methods (`onCreate`, `onViewCreated`, etc.).
* **Dependency Injection**: Hilt provides an implementation for lateinit properties when using Hilt for
  dependency injection. refer the example below.

```kotlin

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}

interface UserRepository {
    fun user(): User
}


class UserRepositoryImpl @Inject constructor() : UserRepository {
    override fun user(): User = User(name = "John Doe", email = "john.doe@example.com")
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun user(): User = userRepository.user()
}
```

## disadvantages of lateinit

* Limited to var Properties: The property is immutable.
* Not Thread-Safe by Default: If multiple threads access and modify the property simultaneously, it
  can lead to inconsistent states or exceptions.
* Potential for Code Smells and Misuse: It can be misused to delay initialization, leading to
  potential NullPointerExceptions.

## Learning Tests

* [LateinitTest.kt](LateinitTest.kt)
* [LateinitAndroidComponentTest.kt](LateinitAndroidComponentTest.kt)

Refer the [`lateinit` VS `by lazy`](../by/by.md)