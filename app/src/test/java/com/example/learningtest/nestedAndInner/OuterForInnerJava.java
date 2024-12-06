package com.example.learningtest.nestedAndInner;

public final class OuterForInnerJava {
    private final ReferenceAvailable outerProperty = ReferenceAvailable.CAN_ACCESS;

    /**
     * @noinspection InnerClassMayBeStatic
     */
    public class Inner {
        public ReferenceAvailable innerFunction() {
            return outerProperty;
        }
    }
}


/*
class OuterForInner {
    private val outerProperty: Int = 33

    inner class Inner {
        fun innerFunction(): Int {
            return outerProperty // hold a reference to an instance of the Outer class
        }
    }
}

public class Outer {
    private static String outerStaticProperty = "Outer Static Property";

    public static class Nested {
        public void nestedMethod() {
            System.out.println("Inside Static Nested Class");
            // Can access static members of outer class
            System.out.println("Accessing: " + outerStaticProperty);
        }
    }
 */