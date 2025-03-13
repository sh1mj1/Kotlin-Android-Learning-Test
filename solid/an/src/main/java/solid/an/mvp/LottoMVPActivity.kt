package solid.an.mvp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.learningtest.solid.an.LottoContract
import solid.an.R

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
