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
