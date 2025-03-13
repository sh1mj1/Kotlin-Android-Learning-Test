package lottery.pure

import kotlin.run

fun main() {
    val view = CustomerView()
    val controller = LottoController(view)

    controller.run {
        val seller = lottoSeller()
        val count = lottoCount(seller)
        val lottoGenerateStrategies = lottoGenerateStrategies(count)
        boughtLotto(count, seller, lottoGenerateStrategies)
    }
}
