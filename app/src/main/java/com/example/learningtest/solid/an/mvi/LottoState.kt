package com.example.learningtest.solid.an.mvi

import com.example.learningtest.solid.pure.Shape

data class LottoState(
    val selectedSellerId: Int? = null,
    val selectedShape: Shape? = null,
    val lottoType: String? = null,
    val manualNumbers: String = "",
    val money: String = "",
    val width: String = "",
    val height: String = "",
    val showWidth: Boolean = false,
    val showHeight: Boolean = false,
    val showManualNumbers: Boolean = false,
    val result: String = "",
    val error: String = "",
)
