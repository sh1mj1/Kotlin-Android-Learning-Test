package lottery.pure

import kotlin.collections.shuffled
import kotlin.collections.sorted
import kotlin.collections.take

/**
 * Model - 로또 판매 시스템
 * */
interface LottoGenerateStrategy {
    fun lotto(): Lottery
}

class RandomLottoGenerateStrategy(
    private val shape: Shape,
) : LottoGenerateStrategy {
    override fun lotto(): Lottery =
        Lottery(
            numbers = (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
            shape,
        )
}

class ManualLottoGenerateStrategy(
    val numbers: List<Int>,
    val shape: Shape,
) : LottoGenerateStrategy {
    override fun lotto(): Lottery =
        Lottery(
            numbers = numbers,
            shape = shape,
        )
}
