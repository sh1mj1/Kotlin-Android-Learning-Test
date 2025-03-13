package solid.an.mvi

data class LottoState(
    val selectedSellerId: Int? = null,
    val selectedShape: lottery.pure.Shape? = null,
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
