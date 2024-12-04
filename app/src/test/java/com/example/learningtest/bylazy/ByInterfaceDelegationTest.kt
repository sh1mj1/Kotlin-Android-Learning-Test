package com.example.learningtest.bylazy

import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe

@DisplayName("Interface Delegation")
class ByInterfaceDelegationTest : FreeSpec({
    "Manual DecoratedCollection" - {
        "can perform delegated methods" {
            val decoratedCollection = DecoratedCollectionManually<Int>()
            decoratedCollection.isEmpty() shouldBe true

            decoratedCollection.add(1) shouldBe true
            decoratedCollection shouldContain 1

            decoratedCollection.addAll(listOf(2, 3)) shouldBe true
            decoratedCollection shouldContainAll listOf(1, 2, 3)
            decoratedCollection.size shouldBe 3
        }

        "newly added method customToString() can be called" {
            val decoratedCollection = DecoratedCollectionManually<Int>()
            decoratedCollection.addAll(listOf(1, 2, 3))
            decoratedCollection.customToString() shouldBe "|1, 2, 3|"
        }
    }

    "By DelegatingCollection" - {
        "add 0 will return false" {
            val delegatedCollection = DelegatedCollectionBy<Int>()
            delegatedCollection.add(0) shouldBe false
        }

        "can be created and perform add operations" {
            val delegatedCollection = DelegatedCollectionBy<Int>()
            delegatedCollection.isEmpty() shouldBe true

            delegatedCollection.add(1) shouldBe true
            delegatedCollection shouldContain 1

            delegatedCollection.addAll(listOf(2, 3)) shouldBe true
            delegatedCollection shouldContainAll listOf(1, 2, 3)
            delegatedCollection.size shouldBe 3
        }

        "newly added method customToString() can be called" {
            val delegatedCollection = DelegatedCollectionBy<Int>()
            delegatedCollection.addAll(listOf(1, 2, 3))
            delegatedCollection.customToString() shouldBe "|1, 2, 3|"
        }
    }
})

/**
 * An interface as simple as Collection, even when you don’t modify any behavior:
 */
private class DecoratedCollectionManually<T> : MutableCollection<T> {
    private val innerList = ArrayList<T>()

    override val size: Int
        get() = innerList.size

    override fun add(element: T): Boolean = innerList.add(element)

    override fun addAll(elements: Collection<T>): Boolean = innerList.addAll(elements)

    override fun clear() {
        innerList.clear()
    }

    override fun iterator(): MutableIterator<T> = innerList.iterator()

    override fun remove(element: T): Boolean = innerList.remove(element)

    override fun removeAll(elements: Collection<T>): Boolean = innerList.removeAll(elements.toSet())

    override fun retainAll(elements: Collection<T>): Boolean = innerList.retainAll(elements.toSet())

    override fun contains(element: T): Boolean = innerList.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = innerList.containsAll(elements)

    override fun isEmpty(): Boolean = innerList.isEmpty()

    override fun toString(): String = innerList.toString()

    // new added function
    fun customToString(): String = innerList.joinToString(prefix = "|", postfix = "|")
}

/**
 * The by-clause in the supertype list for DelegatedCollectionBy indicates that innerList will be stored internally in objects of DelegatedCollectionBy and the compiler will generate all the methods of Base that forward to innerList.
 */
private class DelegatedCollectionBy<T>(
    private val innerList: MutableCollection<T> = ArrayList(),
) : MutableCollection<T> by innerList {
    // possible to override the add method
    override fun add(element: T): Boolean {
        if ((element as? Int) == 0) {
            return false
        }
        return innerList.add(element)
    }

    // new added function
    fun customToString(): String = innerList.joinToString(prefix = "|", postfix = "|")
}
