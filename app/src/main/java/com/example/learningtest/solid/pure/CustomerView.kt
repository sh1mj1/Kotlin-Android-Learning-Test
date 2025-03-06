package com.example.learningtest.solid.pure

/**
 * View - 사용자 인터페이스
 */
class CustomerView {
    fun selectedLottoSeller(): LottoSellerType {
        println("환영합니다! 로또 판매자를 선택해주세요." + "\n")
        println(LottoSellerType.menu)
        print("선택: ")

        val lottoSellerChoice = readln().toIntOrNull()
        requireNotNull(lottoSellerChoice) { "잘못된 입력입니다." }
        require(lottoSellerChoice in 1..4) { "잘못된 입력입니다 1 ~ 4 에서 입력해주세요." }

        println()

        return LottoSellerType[lottoSellerChoice]
    }

    fun paidMoney(): Int {
        print("구매할 금액을 입력하세요: ")
        val money = readln().toIntOrNull()
        requireNotNull(money) { "잘못된 입력입니다." }
        require(money >= 1000) { "1000원 이상의 금액을 입력해주세요." }

        println()
        return money
    }

    fun lottoInputs(count: Int): List<LottoInput> =
        (1..count).map { index ->
            lottoInput(index)
        }

    fun lottoStrategyInput(): LottoStrategy {
        println("어떤 방식으로 로또를 구매하시겠습니까?")
        println(LottoStrategy.menu())
        print("선택: ")

        val input = readln().toIntOrNull()
        requireNotNull(input) { "잘못된 입력입니다." }
        require(input in 1..2) { "1 또는 2를 입력해주세요." }

        println()
        return LottoStrategy[input]
    }

    fun displayLottoNumbers(lottoNumbers: List<Pair<String, String>>) {
        println("구매한 로또 번호:")
        lottoNumbers.forEachIndexed { index, (numbers, shape) ->
            println("${index + 1}번째 로또 ($shape): $numbers")
        }
        println()
    }

    fun showErrorMessage(message: String) {
        println("오류: $message\n")
    }

    fun displayRestMessage(message: String) {
        println("⚡ 잠깐만 기다려 주세요 ~  판매자 휴식 중: $message\n")
    }

    private fun lottoInput(index: Int): LottoInput {
        println("[$index 번째 로또]")
        val lottoStrategy = lottoStrategyInput()
        val lottoShape = lottoShapeInput()
        val manualNumbers =
            if (lottoStrategy == LottoStrategy.MANUAL) {
                manualNumbersInput()
            } else {
                null
            }
        return LottoInput(lottoStrategy, lottoShape, manualNumbers)
    }

    private fun lottoShapeInput(): LottoShapeInput {
        println("로또 모양을 선택하세요:")
        println(LottoShape.menu())
        print("선택: ")

        val lottoShapeChoice = readln().toIntOrNull()
        requireNotNull(lottoShapeChoice) { "잘못된 입력입니다." }
        require(lottoShapeChoice in 1..2) { "1 또는 2를 입력해주세요." }

        val shapeType = LottoShape[lottoShapeChoice]
        val dimensions =
            when (shapeType) {
                LottoShape.RECTANGLE -> {
                    print("직사각형 가로와 세로 길이를 입력하세요 (공백으로 구분): ")

                    val (width, height) =
                        readln().split(" ").mapNotNull { it.toIntOrNull() }.also {
                            require(it.size == 2) { "가로와 세로 두 개의 값을 입력해주세요." }
                        }
                    RectangleLength(listOf(width, height))
                }

                LottoShape.SQUARE -> {
                    print("정사각형 한 변의 길이를 입력하세요: ")
                    val asideLength =
                        listOf(
                            readln().toIntOrNull().also {
                                requireNotNull(it) { "숫자를 입력해야 합니다." }
                            }!!,
                        )

                    SquareLength(asideLength)
                }
            }

        println()
        return LottoShapeInput(shapeType, dimensions)
    }

    private fun manualNumbersInput(): List<Int> {
        print("수동으로 선택할 6개의 숫자를 입력하세요 (공백으로 구분): ")

        val input = readln().split(" ").mapNotNull { it.toIntOrNull() }
        require(input.size == 6) { "6개의 숫자를 입력해주세요." }

        println()
        return input
    }
}

data class LottoInput(
    val strategy: LottoStrategy,
    val shape: LottoShapeInput,
    val manualNumbers: List<Int>?,
)

data class LottoShapeInput(
    val shape: LottoShape,
    val dimensions: ShapeLength,
)

interface ShapeLength {
    val length: List<Int>
}

data class RectangleLength(
    override val length: List<Int>,
) : ShapeLength

data class SquareLength(
    override val length: List<Int>,
) : ShapeLength

enum class LottoSellerType(val value: Int, val content: String, val price: Int) {
    NORMAL(1, "일반 로또 판매자", 1_000),
    DISCOUNT(2, "할인 로또 판매자", 5_00),
    NORMAL_MACHINE(3, "일반 로또 자판기", 1_000),
    NOISY_MACHINE(4, "시끄러운 로또 자판기", 1_000),
    ;

    companion object {
        val menu: String = LottoSellerType.entries.joinToString("\n") { it.menuMessage() } + "\n"

        operator fun get(value: Int): LottoSellerType = LottoSellerType.entries.first { it.value == value }
    }

    private fun menuMessage(): String = "${this.value}. ${this.content} (${this.price}원)"
}

enum class LottoShape(val value: Int, val content: String) {
    RECTANGLE(1, "사각형"),
    SQUARE(2, "정사각형"),
    ;

    companion object {
        operator fun get(value: Int): LottoShape = LottoShape.entries.first { it.value == value }

        fun menu(): String = LottoShape.entries.joinToString("\n") { it.menuMessage() } + "\n"
    }

    private fun menuMessage(): String = "${this.value}. ${this.content}"
}

enum class LottoStrategy(val value: Int, val content: String) {
    AUTO(1, "자동 생성"),
    MANUAL(2, "수동 입력"),
    ;

    companion object {
        operator fun get(value: Int): LottoStrategy = LottoStrategy.entries.first { it.value == value }

        fun menu(): String = LottoStrategy.entries.joinToString("\n") { it.menuMessage() } + "\n"
    }

    private fun menuMessage(): String = "${this.value}. ${this.content}"
}
