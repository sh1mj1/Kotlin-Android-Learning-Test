package com.example.learningtest.kotest

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe

/**
 * FeatureSpec allows you to use feature and scenario, which will be familiar to those who have used cucumber.
 *
 * Although not intended to be exactly the same as cucumber, the keywords mimic the style.
 *
 *  FeatureSpec 를 통해 cucumber 를 사용해본 사람들에게 익숙한 feature 와 scenario 를 사용할 수 있습니다.
 *
 *  cucumber 와 정확히 같은 것을 하려는 것은 아니지만 키워드는 그 스타일과 비슷합니다.
 */
class KotestFeatureSpec : FeatureSpec({
    feature("the can of coke") {
        scenario("should be fizzy when I shake it") {
            val coke = Coke()
            coke.shake()
            coke.isFizzy shouldBe true
        }
        scenario("and should be tasty") {
            val coke = Coke()
            coke.isTasty shouldBe true
        }
    }
})

class Coke {
    private var _isFizzy: Boolean = false
    val isFizzy: Boolean
        get() = _isFizzy

    private var _isTasty: Boolean = true
    val isTasty: Boolean
        get() = _isTasty

    fun shake() {
        _isFizzy = true
    }
}
