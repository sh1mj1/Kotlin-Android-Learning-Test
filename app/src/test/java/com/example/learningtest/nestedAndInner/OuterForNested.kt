package com.example.learningtest.nestedAndInner

class OuterForNested {
    private val outerProperty: ReferenceAvailable = ReferenceAvailable.CAN_ACCESS

    class Nested {
        fun nestedFunction(): ReferenceAvailable {
//            return outerProperty // [COMPILE ERROR] Unresolved reference: outerProperty
            return ReferenceAvailable.CAN_NOT_ACCESS
        }
    }
}
