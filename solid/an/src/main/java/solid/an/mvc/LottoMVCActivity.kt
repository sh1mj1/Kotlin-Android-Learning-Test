package solid.an.mvc

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import lottery.pure.DisCountLottoSeller
import lottery.pure.LottoSeller
import lottery.pure.ManualLottoGenerateStrategy
import lottery.pure.NoisyLottoVendingMachine
import lottery.pure.NormalLottoSeller
import lottery.pure.NormalLottoVendingMachine
import lottery.pure.RandomLottoGenerateStrategy
import lottery.pure.Rectangle
import lottery.pure.Shape
import lottery.pure.Square
import solid.an.R
import kotlin.collections.joinToString
import kotlin.collections.mapNotNull
import kotlin.onFailure
import kotlin.onSuccess
import kotlin.runCatching
import kotlin.text.split
import kotlin.text.toIntOrNull
import kotlin.text.trim

class LottoMVCActivity : AppCompatActivity() {
    private lateinit var radioSellerGroup: RadioGroup
    private lateinit var radioShapeGroup: RadioGroup
    private lateinit var radioLottoTypeGroup: RadioGroup
    private lateinit var inputMoney: EditText
    private lateinit var inputWidth: EditText
    private lateinit var inputHeight: EditText
    private lateinit var inputManualNumbers: EditText
    private lateinit var btnBuyLotto: Button
    private lateinit var txtResult: TextView

    @SuppressLint("SetTextI18n")
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

        radioShapeGroup.setOnCheckedChangeListener { _, checkedId ->
            inputWidth.visibility = EditText.VISIBLE
            inputHeight.visibility =
                if (checkedId == R.id.radioRectangle) EditText.VISIBLE else EditText.GONE
        }

        radioLottoTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            inputManualNumbers.visibility =
                if (checkedId == R.id.radioManual) EditText.VISIBLE else EditText.GONE
        }

        btnBuyLotto.setOnClickListener {
            runCatching { processLottoPurchase() }
                .onSuccess { result -> txtResult.text = result }
                .onFailure { error -> txtResult.text = "⚠️ 오류: " + error.message }
        }
    }

    private fun processLottoPurchase(): String {
        val seller =
            when (radioSellerGroup.checkedRadioButtonId) {
                R.id.radioNormalSeller -> NormalLottoSeller()
                R.id.radioDiscountSeller -> DisCountLottoSeller()
                R.id.radioVendingNormal -> NormalLottoVendingMachine()
                R.id.radioVendingNoisy -> NoisyLottoVendingMachine()
                else -> error("로또 판매자를 선택해주세요.")
            }

        val shape =
            when (radioShapeGroup.checkedRadioButtonId) {
                R.id.radioRectangle ->
                    Rectangle(
                        inputWidth.text.toString().toIntOrNull() ?: error("가로 길이를 입력해주세요."),
                        inputHeight.text.toString().toIntOrNull() ?: error("세로 길이를 입력해주세요."),
                    )

                R.id.radioSquare ->
                    Square(
                        inputWidth.text.toString().toIntOrNull() ?: error("한 변의 길이를 입력해주세요."),
                    )

                else -> error("로또 모양을 선택해주세요.")
            }

        val money = inputMoney.text.toString().toIntOrNull() ?: error("구매할 금액을 입력해주세요.")
        require(money % seller.lottoPrice == 0) { "금액은 로또 가격의 정확한 배수여야 합니다." }

        val lottoType =
            when (radioLottoTypeGroup.checkedRadioButtonId) {
                R.id.radioAuto -> "AUTO"
                R.id.radioManual -> "MANUAL"
                else -> error("로또 구매 방식을 선택해주세요.")
            }

        return buyLotto(seller, shape, money, lottoType)
    }

    private fun buyLotto(
        seller: LottoSeller,
        shape: Shape,
        money: Int,
        lottoType: String,
    ): String {
        require(money >= seller.lottoPrice) { "금액이 부족합니다. 최소 ${seller.lottoPrice}원 이상 입력하세요." }

        val count = money / seller.lottoPrice
        val lottoNumbers =
            List(count) {
                when (lottoType) {
                    "AUTO" -> RandomLottoGenerateStrategy(shape).lotto()
                    "MANUAL" -> {
                        val numbers =
                            inputManualNumbers.text.toString()
                                .split(",")
                                .mapNotNull { it.trim().toIntOrNull() }
                        require(numbers.size == 6) { "6개의 숫자를 입력해주세요." }
                        ManualLottoGenerateStrategy(numbers, shape).lotto()
                    }

                    else -> error("잘못된 로또 구매 방식입니다.")
                }
            }

        return lottoNumbers.joinToString("\n") {
            "Numbers: $lottoNumbers (" +
                "${
                    when (shape) {
                        is Rectangle -> "가로: ${shape.width}, 세로: ${shape.height}"
                        is Square -> "한 변의 길이: ${shape.side}"
                    }
                })"
        }
    }
}
