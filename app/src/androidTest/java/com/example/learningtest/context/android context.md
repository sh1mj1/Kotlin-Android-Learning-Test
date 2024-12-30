# Android Context

You will always encounter the Context class when developing an app.  
Context is very important concept in android development.  
Without Context, you cannot start an activity, broadcast, or service.  
Context is the superclass of many components, and it connects many components through Context.

So understanding Context itself is helpful in understanding the components.
Various components(Activity, Service, Broadcast Receiver, Content Provider) are able to access
system services and application resources through Context.

## What is Android Context?

Context provides information about the current state of the application.  
Context is the interface that allows access to application-specific resources and system service,
and application environment.

The main features are:

* access resource: getResources(), getString(), getDrawable() etc.
* access system service: getSystemService() etc.
* start intent: startActivity(), startService() etc.
* layout inflation: transform XML layout to View using LayoutInflater.

## Context and its subclasses

`Context` is an abstract class.  
`ContextWrapper` and `ContextImpl` extends `Context` class. and ContextWrapper has a reference to
`ContextImpl`.    
`Activity`, `Service`, `Application` are concrete implementation of `ContextWrapper`.  
![img.png](img.png)  
this diagram shows the relationship between `Context`, `ContextWrapper`, and `ContextImpl`.

## You can use multiple ways to use Context

There are multiple ways to get a Context instance.  
For example, in Activity, you can use:

1. `this` (activity instance itself)
2. `getBaseContext()` to get a ContextImpl instance
3. `getApplicationContext()` to get an Application instance

## Application Context VS Activity Context

|                   | Application Context                | Activity Context                                     |
|-------------------|------------------------------------|------------------------------------------------------|
| Lifecycle         | Tied to the application            | Tied to the specific Activity                        |
| Usage Scope       | Global                             | Specific to the Activity                             |
| UI Interaction    | Not suitable                       | Suitable                                             |
| Memory Leak Risk  | Low                                | High (if improperly managed)                         |
| Example Use Cases | Singleton objects such as Database | `Dialogs`, `Snackbar`, Managing views and animations |

Using Application Context where Activity Context is required may cause unexpected behavior or
crashes.

```kotlin
// This works with Application Context
Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT).show()

// This fails with Application Context
AlertDialog.Builder(getApplicationContext())
    .setTitle("Title")
    .setMessage("Message")
    .show() // Will cause an exception

```

## Reference

- Android Developer - [Context](https://developer.android.com/reference/android/content/Context)
- Book: 안드로이드 프로그래밍 Next Step - 노재춘



