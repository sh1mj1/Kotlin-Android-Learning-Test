package com.example.learningtest.solid.an

interface LottoContract {
    interface View {
        fun showResult(result: String)

        fun showError(error: String)

        fun setManualNumbersVisibility(visible: Boolean)

        fun setShapeInputsVisibility(
            showWidth: Boolean,
            showHeight: Boolean,
        )

        fun getWidthInput(): String

        fun getHeightInput(): String

        fun getMoneyInput(): String

        fun getManualNumbersInput(): String?
    }

    interface Presenter {
        fun updateSeller(sellerId: Int)

        fun updateShape(shapeId: Int)

        fun updateLottoType(typeId: Int)

        fun updateMoney()

        fun updateManualNumbers()

        fun buyLotto()
    }
}
