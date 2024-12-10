# Activity

<!-- TOC -->
* [Activity](#activity)
  * [What is an activity? (simple)](#what-is-an-activity-simple)
    * [Why do we need to use activity?](#why-do-we-need-to-use-activity)
    * [the activity that don't show UI (no `setContentView`)](#the-activity-that-dont-show-ui-no-setcontentview)
    * [How does the mobile app experience differ from a traditional desktop experience?](#how-does-the-mobile-app-experience-differ-from-a-traditional-desktop-experience)
<!-- TOC -->

## What is an activity? (simple)

An activity is basically one screen of your app where the user sees and interacts with your
interface.  
It's the main place where the app shows what's important right now.  
And it can manage saving and restoring states if your app gets paused or stopped.  
Activities also make it easier for apps to work together, like when sharing stuff between apps.

### Why do we need to use activity?

Unlike programming paradigms in which apps are launched with a `main()` method,  
the Android system initiates code in an Activity instance by invoking specific callback methods that
correspond to specific stages of its lifecycle.

### the activity that don't show UI (no `setContentView`)

You can have an activity with no UI that basically acts like a traffic controller(front controller)
for the rest of your apps.  
For example, it could take incoming data or intents, then immediately decide which other Activity or
Fragment the user should see next, without showing its own layout.

_What’s the benefit of having an Activity that doesn’t show any UI at all?_

it can simplify navigation.  
it can keep your code cleaner and make it easier to manage the overall user journey.  
Instead of having complex logic scattered around different parts of the app,  
this 'front controller' Activity can handle all the decision-making in one place.  
When the user opens the app, it checks conditions-like whether the user is logged in or has
completed a tutorial-and then sends them straight to the right screen.

### How does the mobile app experience differ from a traditional desktop experience?

Mobile apps often let the user jump in at different screens depending on what’s calling them—like
opening your email app’s compose screen directly from a social media link.  
The Activity class supports this flexibility by letting your app launch at the right place based on
the user’s action, rather than always starting from a single “home” screen.
