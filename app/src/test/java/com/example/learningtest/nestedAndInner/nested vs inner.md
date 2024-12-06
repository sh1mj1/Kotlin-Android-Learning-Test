# nested and inner class (nested by default)

Classes can be nested in other classes.

## `nested` class in kotlin

A nested class in kotlin is a class declared within another class without the `inner` keyword.  
By default, it's static, meaning it doesn't hold a reference to the outer class.

Use nested classes when you want to group related classes without needing access to the outer
class's members.

## `inner` class in kotlin

An inner class in kotlin is a nested class marked with the `inner` keyword.  
It holds a reference to an instance of the outer class and can access its members.

Use inner classes when you need to access the outer class's members.

## But in java

In java, a inner class is default, when you define a class inside another class.  
if you want to define nested class, the class has to be marked with `static` keyword.

![img.png](nested%20vs%20inner.png)



