package com.example.learningtest.nestedAndInner

class OuterForInner {
    val outerProperty: ReferenceAvailable = ReferenceAvailable.CAN_ACCESS

    inner class Inner {
        fun innerFunction(): ReferenceAvailable {
            return outerProperty // hold a reference to an instance of the Outer class
        }
    }
}
