```kotlin
class BasicComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}
```

You use `setContent` to define your layout,  
but instead of using an XML file as you'd do in the traditional View system(like
`setContentView`),  
you call Composable functions within it.

`LearningTestTheme` is a way to style Composable functions.([Theme.kt](../../../ui/theme/Theme.kt))

Surface

You can set a different background color for the `Greeting` by wrapping the Text composable with a
`Surface`.  
Surface takes a color, so use `MaterialTheme.colorScheme.primary`

The components nested inside `Surface` will be drawn on top of that background color.
[GreetingV2.kt](GreetingV2.kt): `GreetingV2` use `Surface`.

Look at the text color.  
The text is now white even we didn't define it.

The Material Components such as `androidx.compose.material3.Surface`, are built to make your
experience better  
by taking taking care of common features that you probably want in your app,  
such as choosing an appropriate color for text.

We say **Material is opinionated** because it provides good defaults and patterns that are common to
most apps.  
The Material components in Compose are built ont top of other foundational components (in
`androidx.compose.foundation`),  
which are also accessible from your app components in case you need more flexibility.

In this case, `Surface` understands that, when the background is set to the `primary` color,  
any text on top of it should use the `onPrimary` color, which is also defined in the theme.

## Modifier

Most Compose Ui elements such as `Surface` and `Text` accept an **optional modifier parameter**.  
`Modifier`s tell a UI element how to lay out, display, or behave within its parent layout.

For example, the padding modifier will apply an amount of space around the element it decorates.    
You can create a padding modifier with `Modifier.padding()`.    
You can also add multiple modifiers by chaining them,  
so in our case we can add the padding modifier to the default one: `modifier.padding(24.dp)`.

Now, add padding to your Text on the screen: [GreetingV3.kt](GreetingV3.kt): GreetingV3 use
`Modifier.padding()` and `Surface`.

Modifiers allow you to decorate or augment a composable. Modifiers let you do these sorts of things:

* Change the `composable`'s size, layout, behavior, and appearance
* Add information, like accessibility labels
* Process user input
* Add high-level interactions, like making an element clickable, scrollable, draggable, or zoomable

## Reusing composables

By making small reusable components it's easy to build up a library of UI elements used in your
app.  
Each one is responsible for one small part of the screen and can be edited independently.

As a best practice, ur function should include a Modifier parameter that is assigned an empty
Modifier by default.  
Forward this modifier to the first composable you call inside your function.  
This way, the calling site can adapt instructions and behaviors from outside of your composable
function.

Create a Composable called MyApp that includes the greeting.

[BasicComposeActivity.kt](../../../BasicComposeActivity.kt)
and [GreetingV3Preview.kt](GreetingV3.kt) reuse `MyApp` composable function.

잘 이해가 안 가는데 조금 이따 또 있는 거 확인하기.

## Columns and Rows

The three basic standard layout elements in Compose are `Column`, `Row` and `Box`.  
![img.png](Basic-standard-layout-elements-in-Compose.png)

They are Composable functions that take Composable content, so you can place items inside.  
For example, each child inside of a `Column` will be placed vertically.

[GreetingV4.kt](GreetingV4.kt): use `Column`.

Composable functions can be used like any other functions in Kotlin.    
This makes building UIs powerful since you can add statements to influence how the UI will be
displayed.  
For example, you can use a `for` loop to add elements to the
`Column`: [BasicComposeActivity.kt](../../../BasicComposeActivity.kt): just like `MyApp2`

[Column's children Test](../../../../../../../../androidTest/java/com/example/learningtest/compose/basic/codelab/GreetingV4KtTest.kt):
use `onParent`.  
if it has only one composable layout, you can use `Modifier.testTag(...)`.

### Add ElevatedButton 
[GreetingV5.kt](GreetingV5.kt)
The `Column` is part of a Row, which contains:

* The Column (with `Modifier.weight(1f)`).
* An `ElevatedButton` (with no weight applied).

Effect of weight(1f) on the Column:

* The Column is instructed to take up all remaining horizontal space in thr `Row` after the
  `ElevatedButton` is measured and laid out.
* Since the `ElevatedButton` doesn't have a weight, it only takes up as much space as it needs to
  display its content (`Text("Show more")`).

The `Column` expands to fill all available space not occupied by the `ElevatedButton`.  
There's no `alignEnd` modifier so, instead, you give some `weight` to the composable at the start.

[ElevatedButton Reference](https://m3.material.io/components/buttons/overview) 

https://developer.android.com/develop/ui/compose/modifiers

https://developer.android.com/codelabs/jetpack-compose-basics#5

