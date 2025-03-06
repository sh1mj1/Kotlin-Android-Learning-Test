package com.example.learningtest.solid.pure

/**
 * View - 사용자 인터페이스
 */
class CustomerView {
    fun selectedLottoSeller(): LottoSellerType {
        println("환영합니다! 로또 판매자를 선택해주세요." + "\n")

        retryInput {
            println(LottoSellerType.menu)
            print("선택: ")
            val lottoSellerChoice = readln().toIntOrNull()
            requireNotNull(lottoSellerChoice) { "잘못된 입력입니다." }
            require(lottoSellerChoice in 1..4) { "잘못된 입력입니다 1 ~ 4 에서 입력해주세요." }

            println()

            return LottoSellerType[lottoSellerChoice]
        }
    }

    fun paidMoney(): Int {
        retryInput {
            print("구매할 금액을 입력하세요: ")
            val money = readln().toIntOrNull()
            requireNotNull(money) { "잘못된 입력입니다." }

            println()
            return money
        }
    }

    fun lottoInputs(count: Int): List<LottoInput> =
        (1..count).map { index ->
            println("[$index 번째 로또]")
            lottoStrategyInput()
        }

    fun lottoStrategyInput(): LottoInput {
        val lottoStrategyMenu =
            retryInput {
                println("어떤 방식으로 로또를 구매하시겠습니까?")
                println(LottoStrategyMenu.displaying())
                print("선택: ")

                val input = readln().toIntOrNull()
                requireNotNull(input) { "잘못된 입력입니다." }
                when (input) {
                    1 -> LottoStrategyMenu.AUTO
                    2 -> LottoStrategyMenu.MANUAL
                    else -> error("1 또는 2를 입력해주세요.")
                }
            }
        println()

        return when (lottoStrategyMenu) {
            LottoStrategyMenu.AUTO -> autoLottoInput()
            LottoStrategyMenu.MANUAL -> manualLottoInput()
        }
    }

    fun displayLottoNumbers(lottoes: List<LottoOutput> = listOf()) {
        println("구매한 로또 번호:")
        lottoes.forEachIndexed { index, (shape, numbers) ->
            println("${index + 1}번째 로또 (${shape.content}): $numbers")
        }
        println()
    }

    fun displayRest(message: String) {
        println("⚡ 잠깐만 기다려 주세요 ~  판매자 휴식 중: $message\n")
    }

    fun displayError(message: String) {
        println("⚠️ 에러: $message\n")
    }

    private fun autoLottoInput(): LottoInput {
        retryInput {
            val lottoShape = lottoShapeInput()
            return AutoLottoInput(LottoStrategyMenu.AUTO, lottoShape)
        }
    }

    private fun manualLottoInput(): LottoInput {
        retryInput {
            val lottoShape = lottoShapeInput()
            val manualNumbers = manualNumbersInput()

            return ManualLottoInput(LottoStrategyMenu.MANUAL, lottoShape, manualNumbers)
        }
    }

    private fun lottoShapeInput(): LottoShapeInput {
        val shapeType =
            retryInput {
                println("로또 모양을 선택하세요:")
                println(LottoShape.menu())
                print("선택: ")

                val lottoShapeChoice = readln().toIntOrNull()
                requireNotNull(lottoShapeChoice) { "잘못된 입력입니다." }
                require(lottoShapeChoice in 1..2) { "1 또는 2를 입력해주세요." }
                LottoShape[lottoShapeChoice]
            }

        val dimensions =
            retryInput {
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
                                readln().toIntOrNull().let {
                                    requireNotNull(it) { "숫자를 입력해야 합니다." }
                                    it
                                },
                            )
                        SquareLength(asideLength)
                    }
                }
            }

        println()
        return LottoShapeInput(shapeType, dimensions)
    }

    private fun manualNumbersInput(): List<Int> {
        retryInput {
            print("수동으로 선택할 6개의 숫자를 입력하세요 (공백으로 구분): ")

            val input = readln().split(" ").mapNotNull { it.toIntOrNull() }
            require(input.size == 6) { "6개의 숫자를 입력해주세요." }

            println()
            return input
        }
    }
}

sealed class LottoInput {
    abstract val strategy: LottoStrategyMenu
    abstract val shape: LottoShapeInput
}

data class AutoLottoInput(
    override val strategy: LottoStrategyMenu,
    override val shape: LottoShapeInput,
) : LottoInput()

data class ManualLottoInput(
    override val strategy: LottoStrategyMenu,
    override val shape: LottoShapeInput,
    val numbers: List<Int>,
) : LottoInput()

data class LottoShapeInput(
    val shape: LottoShape,
    val dimensions: ShapeLength,
)

data class LottoOutput(
    val shape: LottoShape,
    val numbers: String,
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

enum class LottoStrategyMenu(val value: Int, val content: String) {
    AUTO(1, "자동 생성"),
    MANUAL(2, "수동 입력"),
    ;

    companion object {
        operator fun get(value: Int): LottoStrategyMenu = LottoStrategyMenu.entries.first { it.value == value }

        fun displaying(): String = LottoStrategyMenu.entries.joinToString("\n") { it.menuMessage() } + "\n"
    }

    private fun menuMessage(): String = "${this.value}. ${this.content}"
}

inline fun <T> retryInput(block: () -> T): T {
    while (true) {
        try {
            return block()
        } catch (e: IllegalArgumentException) {
            println("\n ⚠️ 입력 오류: ${e.message}\n다시 입력해주세요. \n")
        } catch (e: Exception) {
            println("\n ⚠️ 알 수 없는 오류 발생: ${e.message}\n다시 입력해주세요.\n")
        }
    }
}
