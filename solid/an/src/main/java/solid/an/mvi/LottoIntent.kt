package solid.an.mvi

sealed class LottoIntent {
    data class SelectSeller(val sellerId: Int) : LottoIntent()

    data class SelectShape(val shapeId: Int) : LottoIntent()

    data class SelectLottoType(val typeId: Int) : LottoIntent()

    data class EnterMoney(val amount: String) : LottoIntent()

    data class EnterWidth(val width: String) : LottoIntent()

    data class EnterHeight(val height: String) : LottoIntent()

    data class EnterManualNumbers(val numbers: String) : LottoIntent()

    object BuyLotto : LottoIntent()
}
