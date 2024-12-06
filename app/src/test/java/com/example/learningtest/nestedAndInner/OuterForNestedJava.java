package com.example.learningtest.nestedAndInner;

public final class OuterForNestedJava {
    private final ReferenceAvailable outerProperty = ReferenceAvailable.CAN_ACCESS;

    public static class Nested {
        public ReferenceAvailable nestedFunction() {
            // [COMPILE ERROR] Non-static field 'outerProperty' cannot be referenced from a static context
            return ReferenceAvailable.CAN_NOT_ACCESS;
        }
    }
}

/*
class OuterForNested {
    private val outerProperty: Int = 33

    class Nested {
        fun nestedFunction(): Int {
//            return outerProperty // [COMPILE ERROR] Unresolved reference: outerProperty
            return 10
        }
    }
}
 */