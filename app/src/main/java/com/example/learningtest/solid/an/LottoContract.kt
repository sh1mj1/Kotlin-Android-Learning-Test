package com.example.learningtest.solid.an

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.learningtest.R
import com.example.learningtest.solid.pure.DisCountLottoSeller
import com.example.learningtest.solid.pure.LottoSeller
import com.example.learningtest.solid.pure.LottoSellerResult
import com.example.learningtest.solid.pure.ManualLottoGenerateStrategy
import com.example.learningtest.solid.pure.NoisyLottoVendingMachine
import com.example.learningtest.solid.pure.NormalLottoSeller
import com.example.learningtest.solid.pure.NormalLottoVendingMachine
import com.example.learningtest.solid.pure.RandomLottoGenerateStrategy
import com.example.learningtest.solid.pure.Rectangle
import com.example.learningtest.solid.pure.Shape
import com.example.learningtest.solid.pure.ShapeResult
import com.example.learningtest.solid.pure.Square

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

class LottoPresenter(private val view: LottoContract.View) : LottoContract.Presenter {
    private var selectedSeller: LottoSeller? = null
    private var selectedShape: Shape? = null
    private var lottoType: String? = null
    private var money: Int? = null
    private var manualNumbers: List<Int>? = null

    override fun updateSeller(sellerId: Int) {
        selectedSeller =
            when (sellerId) {
                R.id.radioNormalSeller -> NormalLottoSeller()
                R.id.radioDiscountSeller -> DisCountLottoSeller()
                R.id.radioVendingNormal -> NormalLottoVendingMachine()
                R.id.radioVendingNoisy -> NoisyLottoVendingMachine()
                else -> null
            }
    }

    override fun updateShape(shapeId: Int) {
        when (shapeId) {
            R.id.radioRectangle ->
                view.setShapeInputsVisibility(
                    showWidth = true,
                    showHeight = true,
                )

            R.id.radioSquare -> view.setShapeInputsVisibility(showWidth = true, showHeight = false)
            else -> view.setShapeInputsVisibility(showWidth = false, showHeight = false)
        }
        selectedShape = null
    }

    override fun updateLottoType(typeId: Int) {
        lottoType =
            when (typeId) {
                R.id.radioAuto -> "AUTO"
                R.id.radioManual -> "MANUAL"
                else -> null
            }
        view.setManualNumbersVisibility(typeId == R.id.radioManual)
    }

    override fun updateMoney() {
        money = view.getMoneyInput().toIntOrNull()
    }

    override fun updateManualNumbers() {
        manualNumbers =
            view.getManualNumbersInput()?.split(",")?.mapNotNull { it.trim().toIntOrNull() }
    }

    override fun buyLotto() {
        runCatching {
            val seller = selectedSeller ?: error("로또 판매자를 선택해주세요.")
            val amount = money ?: error("구매할 금액을 입력해주세요.")

            when (val result = seller.lottoCount(amount)) {
                is LottoSellerResult.Failure.InsufficientFunds -> error("금액이 부족합니다. 최소 ${result.requiredAmount}원 이상 입력하세요.")
                is LottoSellerResult.Failure.InvalidAmount -> error("금액은 ${result.requiredUnit}원 단위로 입력해야 합니다.")
                is LottoSellerResult.Success -> {}
            }

            val shape =
                when (view.getWidthInput().toIntOrNull()) {
                    null -> error("로또 모양을 선택해주세요.")
                    else ->
                        when (view.getHeightInput().toIntOrNull()) {
                            null -> Square.create(view.getWidthInput().toInt())
                            else ->
                                Rectangle.create(
                                    view.getWidthInput().toInt(),
                                    view.getHeightInput().toInt(),
                                )
                        }
                }

            val selectedShape =
                when (shape) {
                    is ShapeResult.Success -> shape.value
                    is ShapeResult.Failure.InvalidShapeSize -> error("모양의 길이가 올바르지 않습니다.")
                }

            val type = lottoType ?: error("로또 구매 방식을 선택해주세요.")
            if (type == "MANUAL") {
                val numbers = manualNumbers ?: error("수동 로또 번호를 입력해주세요.")
                require(numbers.size == 6) { "6개의 숫자를 입력해주세요." }
            }

            val count = amount / seller.lottoPrice
            val lottoNumbers =
                List(count) {
                    when (type) {
                        "AUTO" -> RandomLottoGenerateStrategy(selectedShape).lotto()
                        "MANUAL" ->
                            ManualLottoGenerateStrategy(
                                manualNumbers!!,
                                selectedShape,
                            ).lotto()

                        else -> error("잘못된 로또 구매 방식입니다.")
                    }
                }

            lottoNumbers.joinToString("\n") {
                "Numbers: ${it.numbers} " +
                    "(${
                        if (selectedShape is Rectangle) {
                            "가로: ${selectedShape.width}, 세로: ${selectedShape.height}"
                        } else {
                            "한 변의 길이: ${(selectedShape as Square).side}"
                        }
                    })"
            }
        }
            .onSuccess { result -> view.showResult(result) }
            .onFailure { error -> view.showError(error.message ?: "알 수 없는 오류") }
    }
}

class LottoMVPActivity : AppCompatActivity(), LottoContract.View {
    private lateinit var presenter: LottoPresenter
    private lateinit var radioSellerGroup: RadioGroup
    private lateinit var radioShapeGroup: RadioGroup
    private lateinit var radioLottoTypeGroup: RadioGroup
    private lateinit var inputMoney: EditText
    private lateinit var inputWidth: EditText
    private lateinit var inputHeight: EditText
    private lateinit var inputManualNumbers: EditText
    private lateinit var btnBuyLotto: Button
    private lateinit var txtResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_lotto)

        radioSellerGroup = findViewById(R.id.radioSellerGroup)
        radioShapeGroup = findViewById(R.id.radioShapeGroup)
        radioLottoTypeGroup = findViewById(R.id.radioLottoTypeGroup)
        inputMoney = findViewById(R.id.inputMoney)
        inputWidth = findViewById(R.id.inputWidth)
        inputHeight = findViewById(R.id.inputHeight)
        inputManualNumbers = findViewById(R.id.inputManualNumbers)
        btnBuyLotto = findViewById(R.id.btnBuyLotto)
        txtResult = findViewById(R.id.txtResult)

        presenter = LottoPresenter(this)

        radioSellerGroup.setOnCheckedChangeListener { _, id -> presenter.updateSeller(id) }
        radioShapeGroup.setOnCheckedChangeListener { _, id -> presenter.updateShape(id) }
        radioLottoTypeGroup.setOnCheckedChangeListener { _, id -> presenter.updateLottoType(id) }

        btnBuyLotto.setOnClickListener {
            presenter.updateMoney()
            presenter.updateManualNumbers()
            presenter.buyLotto()
        }
    }

    override fun showResult(result: String) {
        txtResult.text = result
    }

    override fun showError(error: String) {
        txtResult.text = "⚠️ 오류: $error"
    }

    override fun setManualNumbersVisibility(visible: Boolean) {
        inputManualNumbers.visibility =
            if (visible) android.view.View.VISIBLE else android.view.View.GONE
    }

    override fun setShapeInputsVisibility(
        showWidth: Boolean,
        showHeight: Boolean,
    ) {
        inputWidth.visibility = if (showWidth) android.view.View.VISIBLE else android.view.View.GONE
        inputHeight.visibility =
            if (showHeight) android.view.View.VISIBLE else android.view.View.GONE
    }

    override fun getWidthInput(): String = inputWidth.text.toString()

    override fun getHeightInput(): String = inputHeight.text.toString()

    override fun getMoneyInput(): String = inputMoney.text.toString()

    override fun getManualNumbersInput(): String? = inputManualNumbers.text.toString().takeIf { it.isNotBlank() }
}
