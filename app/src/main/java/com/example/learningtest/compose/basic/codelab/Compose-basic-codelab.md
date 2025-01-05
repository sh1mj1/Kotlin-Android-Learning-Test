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
[GreetingWithSurface.kt](GreetingWithSurface.kt)

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


