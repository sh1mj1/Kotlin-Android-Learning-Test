package com.example.learningtest.solid.pure

/**
 * Model - 로또 판매 시스템
 * */
interface RestAction {
    fun onRest(): String
}

class ChatCapable(private val message: String) : RestAction {
    override fun onRest(): String = chat()

    fun chat(): String = message
}

class ResetCapable(private val message: String) : RestAction {
    override fun onRest(): String = reset()

    fun reset(): String = message
}

class RestActions(
    private val restActions: List<RestAction>,
) {
    constructor(vararg restActions: RestAction) : this(restActions.toList())

    fun onRest(): String = restActions.joinToString("\n") { it.onRest() }
}
