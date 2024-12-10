Four major components
--
![Four major android app components.png](Four%20major%20android%20app%20components.png)

App components are the essential building blocks of an Android app.   
Each component is an entry point through which the system or a user can enter your app.   
Some components depend on others.

There are four types of app components:

* [Activities](activity/activity.md)
    * It **represents a single screen with a user interface**.
* Services
    * It is a general-purpose entry point for **keeping an app running in the background** for
      all kinds of reasons.
    * Does not provide a user interface.
* Broadcast receivers
    * It is a component that **lets the system deliver the events** to the app outside
      of a regular user flow so the app can respond to system-wide broadcast announcements.
* Content providers
    * It **manages a shared set of app data that you can store in**:
        * the file system, a SQLite database, on the web, on any other persistent storage location
          that your app can access.

Each type serves a distinct purpose and has a distinct lifecycle.  
The lifecycle defines how a component created and destroyed.

Reference:

* https://developer.android.com/guide/components/fundamentals
*

image : https://medium.com/@Abderraouf/understand-android-basics-part-1-application-activity-and-lifecycle-b559bb1e40e

TODO: component link
