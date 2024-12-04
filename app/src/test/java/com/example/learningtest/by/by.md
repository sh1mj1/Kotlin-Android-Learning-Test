<!-- TOC -->
* [by](#by)
  * [Decorator Pattern](#decorator-pattern)
  * [Disadvantages of Delegation using by](#disadvantages-of-delegation-using-by)
  * [use scenarios for `by` in android production](#use-scenarios-for-by-in-android-production)
  * [By lazy](#by-lazy)
  * [lateinit vs by lazy](#lateinit-vs-by-lazy)
<!-- TOC -->

# by

The `by` keyword is used for delegation,   
allowing a class to **delegate the implementation of
certain functionalities to another object.**  
You can easily implement the decoration pattern using the `by` keyword.  
The primary uses of by include: [Property Delegation](ByPropertyDelegationTest.kt),
[Interface Delegation](ByInterfaceDelegationTest.kt)

## Decorator Pattern

You often need to add behavior to another class, even if it wasn’t designed to be extended.    
A commonly used way to implement this is known as the Decorator pattern.  
The essence of the pattern is that a new class is created, implementing the same interface as the
original class and storing the instance of the original class as a field.   
Methods in which the behavior of the original class doesn’t need to be modified are forwarded to the
original class instance.
But this approach requires a large amount of boilerplate code

[the example for manual decorator pattern](ByInterfaceDelegationTest.kt)

## Disadvantages of Delegation using by

* Performance Overhead (Minor): Issue: Delegation introduces an additional layer of method calls.

## use scenarios for `by` in android production

* [Initializing ViewModel in an Activity](android/CreateViewModelLazilyTest.kt)
    * This approach leverages Kotlin's property delegation to handle the lifecycle-aware creation
      and retention of ViewModel instances.
    * Automatically handles the creation and retention of the ViewModel instance tied to the
      Activity's lifecycle.

* [Initializing Expensive or singleton Resources](android/CreateRetrofitLazilyTest.kt)
    * Defers initialization until the resource is actually needed, improving app startup time.
    * Ensures the singleton is created only once and when needed.
* View Binding Initialization:
    * Ensures that binding is set up only when the views are accessed, avoiding premature
      initialization.
    * refer the example below.

```kotlin
class StubActivity : ComponentActivity() {

    private val binding: ActivityStubBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textView.text = "Hello, Android!"
    }
}

```

## By lazy

`by lazy` is a Kotlin delegate that initializes a property only when it's accessed for the first
time.

* Performance Optimization: Delays expensive operations until necessary.
* Resource Management: Saves memory by avoiding unnecessary initializations.
* Thread Safety: By default, initialization is thread-safe (`LazyThreadSafetyMode.SYNCHRONIZED`).

## lateinit vs by lazy

|                               | `lateinit`                                    | `by lazy`                          |
|-------------------------------|-----------------------------------------------|------------------------------------|
| Used with                     | `var `                                        | `val`                              |
| initialization time           | must be initialized before first use          | initialized upon first access      |
| Nullability                   | can not be used with null                     | can be used with nullable types    |
| usage scope                   | class properties only                         | class properties & local variables |
| primitive types               | not allowed                                   | allowed                            |     
| Thread safety                 | not thread-safe                               | thread-safe by default             |     
| Error on Uninitialized Access | Throws `UninitializedPropertyAccessException` | -                                  |     

