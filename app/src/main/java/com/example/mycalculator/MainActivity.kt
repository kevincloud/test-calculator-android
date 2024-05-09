package com.example.mycalculator

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.example.mycalculator.ui.theme.MyCalculatorTheme
import com.launchdarkly.sdk.*
import com.launchdarkly.sdk.android.*
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class MainActivity : ComponentActivity() {

    private lateinit var btnOne : Button
    private lateinit var btnTwo : Button
    private lateinit var btnThree : Button
    private lateinit var btnFour : Button
    private lateinit var btnFive : Button
    private lateinit var btnSix : Button
    private lateinit var btnSeven : Button
    private lateinit var btnEight : Button
    private lateinit var btnNine : Button
    private lateinit var btnZero : Button

    private lateinit var btnDivide : Button
    private lateinit var btnMultiply : Button
    private lateinit var btnMinus : Button
    private lateinit var btnPlus : Button
    private lateinit var btnClear : Button
    private lateinit var btnEqual : Button
    private lateinit var btnDot : Button
    private lateinit var btnBack : Button

    private lateinit var tvInput : TextView
    private lateinit var client: LDClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var actLogin: LoginActivity

        
        setContentView(R.layout.activity_main)
        tvInput = findViewById<TextView>(R.id.textview_input)

        // Initialize the LaunchDarkly Client
        val ldConfig: LDConfig = LDConfig.Builder(LDConfig.Builder.AutoEnvAttributes.Enabled)
            .mobileKey("mob-e978a2b8-b8ea-49c5-b7f8-668a3339de7f")
            .build()
        val context = LDContext.builder("018f363f-b97b-7d74-96d5-be7594b7f74d")
            .kind("device")
            .name("Linux")
            .build()
        client = LDClient.init(application, ldConfig, context, 0)

        // Register a flag change listener / subscribe to a flag
        flagTestFlag()

        val listener = FeatureFlagChangeListener {
            flagTestFlag()
        }

        client.registerFeatureFlagListener("test-flag", listener)

        buttonSetup()
    }

    private fun flagTestFlag() {
        val flagKey = "test-flag"
        val flagValue = client.boolVariation(flagKey, false)
        if (flagValue) {
            tvInput.setBackgroundColor(Color.Cyan.toArgb())
        } else {
            tvInput.setBackgroundColor(Color.LightGray.toArgb())
        }
    }

    private fun setInput(value: String) {
        if (tvInput.text.toString().trim() == "0") {
            if (value != "0") {
                tvInput.text = value
            }
        } else {
            tvInput.append(value);
        }
    }

    private fun setOperator(op: String) {
        when (op) {
            "BACK" -> {
                if (tvInput.length() <= 1) {
                    tvInput.text = "0"
                } else {
                    val str: String = tvInput.text.toString()
                    tvInput.text = str.substring(0, str.length - 1)
                }
            }
            "CLEAR" -> {
                tvInput.text = "0"
            }
            "DOT" -> {
                val str: String = tvInput.text.toString()
                val mySet: List<String> = str.split('+', '-', '×', '÷')
                if (mySet[mySet.size - 1].isEmpty()) {
                    tvInput.append("0.")
                } else if (!mySet[mySet.size - 1].contains('.')) {
                    tvInput.append(".")
                }
            }
            "EQUAL" -> {
                tvInput.text = evalExpr(tvInput.text.toString())
            }
            else -> {
                val str: String = tvInput.text.toString()
                if (str.isNotEmpty()) {
                    val x: String = str.substring(str.length - 1)
                    if ("+-×÷".contains(x)) {
                        val newText: String = str.substring(0, str.length - 1) + op
                        tvInput.text = newText
                    } else {
                        tvInput.append(op)
                    }
                }
            }
        }
    }

    private fun evalExpr(value: String): String {
        val str = value.replace("-", "+-")
        val addOps: List<String> = str.split('+')
        var total : BigDecimal = BigDecimal(0.0)
        for (addOp in addOps) {
            if (addOp.contains("×") || addOp.contains("÷")) {
                total = total.add(doMultiply(addOp))
            } else {
                total = total.add(BigDecimal(addOp))
            }
        }

        return total.toString()
    }

    private fun doMultiply(value: String): BigDecimal {
        var finalTotal: BigDecimal = BigDecimal(1.0)
        val str: String = value.replace("÷", "×1/")
        val newValue: List<String> = str.split("×")
        for (i in 0..newValue.size - 1) {
            if (newValue[i].startsWith("1/")) {
                var tmp: BigDecimal = BigDecimal(newValue[i].substring(2))
                try {
                    tmp = BigDecimal.ONE.divide(tmp)
                } catch(e: Exception) {
                    tmp = BigDecimal.ONE.divide(tmp, MathContext(9, RoundingMode.HALF_EVEN))
                }
                finalTotal = finalTotal.multiply(tmp)
            } else {
                finalTotal = finalTotal.multiply(BigDecimal(newValue[i]))
            }
        }

        return finalTotal
    }

    private fun buttonSetup() {
        btnOne = findViewById<Button>(R.id.btnOne)
        btnOne.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }
        btnTwo = findViewById<Button>(R.id.btnTwo)
        btnTwo.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }
        btnThree = findViewById<Button>(R.id.btnThree)
        btnThree.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }
        btnFour = findViewById<Button>(R.id.btnFour)
        btnFour.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }
        btnFive = findViewById<Button>(R.id.btnFive)
        btnFive.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }
        btnSix = findViewById<Button>(R.id.btnSix)
        btnSix.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }
        btnSeven = findViewById<Button>(R.id.btnSeven)
        btnSeven.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }
        btnEight = findViewById<Button>(R.id.btnEight)
        btnEight.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }
        btnNine = findViewById<Button>(R.id.btnNine)
        btnNine.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }
        btnZero = findViewById<Button>(R.id.btnZero)
        btnZero.setOnClickListener { view ->
            setInput((view as Button).text.toString())
        }

        btnDivide = findViewById<Button>(R.id.btnDivide)
        btnDivide.setOnClickListener { _ ->
            setOperator("÷")
        }
        btnMultiply = findViewById<Button>(R.id.btnMultiply)
        btnMultiply.setOnClickListener { _ ->
            setOperator("×")
        }
        btnMinus = findViewById<Button>(R.id.btnMinus)
        btnMinus.setOnClickListener { _ ->
            setOperator("-")
        }
        btnPlus = findViewById<Button>(R.id.btnPlus)
        btnPlus.setOnClickListener { _ ->
            setOperator("+")
        }
        btnClear = findViewById<Button>(R.id.btnClear)
        btnClear.setOnClickListener { _ ->
            setOperator("CLEAR")
        }
        btnEqual = findViewById<Button>(R.id.btnEqual)
        btnEqual.setOnClickListener { _ ->
            setOperator("EQUAL")
        }
        btnDot = findViewById<Button>(R.id.btnDot)
        btnDot.setOnClickListener { _ ->
            setOperator("DOT")
        }
        btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener { _ ->
            setOperator("BACK")
        }
    }
}
