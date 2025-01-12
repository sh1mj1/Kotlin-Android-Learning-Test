# study state in compose codelab

While the state of the app offers a description of what to display in the UI, events are the
mechanism through which the state changes, resulting in changes to the UI.

Key idea: State is. Events happen.

Events notify a part of a program that something has happened. In all Android apps, there's a core
UI update loop that goes like this:

![img.png](Ui-update-loop.png)

## Mistake for trying to change state

```kotlin
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column

@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        var count = 0
        Text("You've had $count glasses.")
        Button(onClick = { count++ }, Modifier.padding(top = 8.dp)) {
            Text("Add one")
        }
    }
}
```

it doesn't work.

## Memory in a composable function

* The Composition: a description of the UI built by Jetpack Compose when it executes composables.
* Initial composition: creation of a Composition by running composables the first time.
* Recomposition: re-running composables to update the Composition when data changes.

Compose needs to know what state to track.

Compose has a special state tracking system in place that schedules recompositions for any
composables that read a particular state.
This lets Compose be granular and just recompose those composable functions that need to change, not
the whole UI.

You can use the mutableStateOf function to create an observable MutableState.
You can think of using remember as a mechanism to store a single object in the Composition, in the
same way a private val property does in an object.

## Remember in Composition

`remember` stores objects in the Composition,and forgets the object  
if the source location where remember is called is not invoked again during a recomposition.

check the [WaterCounterV2.kt](../WaterCounterV2.kt)

#### 1. Initial State

![img.png](WaterCounterV2 initial state .png)

#### 2. Click Add one button

![img_1.png](2WaterCounterV2-click-add-one-button.png)
![img_7.png](2WaterCounterV2-click-add-one-button-image.png)

#### 3. Click task close IconButton

![img_3.png](3WaterCounterV2-click-task-close-IconButton.png)

#### 4. Click Add one button

![img_4.png](4WaterCounterV2-click-Add-one-button.png)

#### 5. Click clear water count button

![img_5.png](5WaterCounterV2-click-clear-water-count-button.png)
![img_8.png](5WaterCounterV2-click-clear-water-count-button-image.png)

#### 6. Click Add one button

![img_6.png](6WaterCounterV2-click-add-one-button.png)

## Restore state in Compose

Use `rememberSaveable` to restore your UI state after an Activity is recreated.  
Besides retaining state across recompositions,  
`rememberSaveable` also retains state across Activity recreation and system-initiated process death.

## State hoisting

A composable that uses remember to store an object contains internal state, which makes the composable stateful.  
composables with internal state tend to be less reusable and harder to test.  

Composables that don't hold any state are called stateless composables.  
An easy way to create a stateless composable is by using state hoisting.  

The general pattern for state hoisting in Jetpack Compose is to replace the state variable with two parameters:

* `value: T` - the current value to display
* `onValueChange: (T) -> Unit` - an event that requests the value to change with a new value T

where this value represents any state that could be modified.

The pattern where the state goes down, and events go up is called Unidirectional Data Flow (UDF),  
and state hoisting is how we implement this architecture in Compose.  
You can learn more about this in the [Compose Architecture documentation](https://developer.android.com/develop/ui/compose/architecture#udf-compose).  

State that is hoisted this way has some important properties:

* Single source of truth: By moving state instead of duplicating it, we're ensuring there's only one source of truth. This helps avoid bugs.
* Shareable: Hoisted state can be shared with multiple composables.
* Interceptable: Callers to the stateless composables can decide to ignore or modify events before changing the state.
* Decoupled: The state for a stateless composable function can be stored anywhere. For example, in a ViewModel.

### Stateful vs Stateless

A stateless composable is a composable that doesn't own any state, meaning it doesn't hold or define or modify new state.

A stateful composable is a composable that owns a piece of state that can change over time.

In real apps, having a 100% stateless composable can be difficult to achieve depending on the composable's responsibilities.    
You should design your composables in a way that they will own as little state as possible and allow the state to be hoisted, when it makes sense, by exposing it in the composable's API.    

[StatelessCounter.kt](../WaterStatelessCounter.kt) , [StatefulCounter.kt](../WaterStatefulCounter.kt)






