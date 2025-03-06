package com.example.learningtest.solid.pure

/**
 * Model - 로또 판매 시스템
 * */
abstract class LottoSeller() {
    abstract val restActions: RestActions
    abstract val lottoPrice: Int
    private var _restRequired: Boolean = true
    val restRequired: Boolean
        get() = _restRequired

    fun lottoCount(money: Int): Int {
        require(money >= lottoPrice) { "Pay an amount that is greater than or equal to the ticket price." }
        val count = money / lottoPrice
        require(money % lottoPrice == 0) { "Pay an amount that is exactly divisible by the ticket price." }
        return count
    }

    fun soldLotto(lottoGenerateStrategies: List<LottoGenerateStrategy>): List<Lottery> {
        return lottoGenerateStrategies.map(LottoGenerateStrategy::lotto)
    }

    fun onRest(): String = restActions.onRest()
}

class NormalLottoSeller : LottoSeller() {
    override val restActions: RestActions =
        RestActions(
            ChatCapable(message = "Chatting -- Hello!"),
        )
    override val lottoPrice: Int = 1_000
}

class DisCountLottoSeller : LottoSeller() {
    override val restActions: RestActions =
        RestActions(ChatCapable(message = "Chatting -- Good morning!"))
    override val lottoPrice: Int = 500
}

class NormalLottoVendingMachine : LottoSeller() {
    override val restActions: RestActions =
        RestActions(ResetCapable(message = "Resetting -- Quietly"))
    override val lottoPrice: Int = 1_000
}

class NoisyLottoVendingMachine : LottoSeller() {
    override val restActions: RestActions =
        RestActions(ResetCapable(message = "Resetting -- With noise"))
    override val lottoPrice: Int = 1_000
}
