# Intent

## What is Intent?

Intent is a message object that requests an action from another app component.  
It is like a message you send to the system saying, "Hey, I want to do something or go somewhere."  
It could be starting another activity, launching a service, or even opening a webpage.  
And the system analyzes the Intent and executes the component.

## Intent Structure

The primary pieces of information in an intent are:

* action: The general action to be performed, such as ACTION_VIEW.
* data: The data to operate on, such as a person record in the contacts database.

## Intent Resolution

Explicit Intent:
An explicit intent says exactly which component you want to start—like telling your phone “Go run
ThisActivity right now.” An implicit intent is more like “I want to do this type of action, whoever
can handle it, step forward.” The system then picks the best match from the apps that can handle
that request.

* Explicit Intent
    * It is used when you specify a component name in the intent so you know for sure what to call.
    * An explicit intent says exactly which component you want to start like telling your phone
      "Go run XXXActivity right now".
* Implicit Intent
    * It is used to find apps or components that can perform components such as Action or Data
      without specifying a specific component.
    * An implicit intent is more like
      "I want to do this type of action, whoever can handle it, step forward"
    * The system then picks the best match from the apps that can handle that request.

# PendingIntent

A PendingIntent is like a wrapper around an intent that you hand over to another app or service.  
And it allows them to perform that action on your behalf later or in a different context.  
It is useful when you wanna do that, but you still want control over what happens.

For example, in notifications:
you create a PendingIntent so the notification manager can launch an activity when the user taps
it,  
without needing your app to actively be running at that moment.  
It's about delegating responsibility while keeping security and intent consistency in check.  
It uses the **permissions of the app that originally created it**, rather than another process
calling the PendingIntent.  
