package com.assignments.assignment1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EmiCalcActivity : AppCompatActivity () {
    // function to calculate monthly payment
    fun calculateMonthlyPayment(principal: Double, annualInterestRate: Double, termInYears: Int): Double {
        val monthlyInterestRate = annualInterestRate / 100 / 12
        val numberOfPayments = termInYears * 12
        return if (monthlyInterestRate == 0.0) {
            principal / numberOfPayments
        } else {
            val factor = Math.pow(1 + monthlyInterestRate, numberOfPayments.toDouble())
            principal * monthlyInterestRate * factor / (factor - 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emi_calc)

        // back button to return to MainActivity
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }


        val amountInput = findViewById<EditText>(R.id.mortgageAmountInput)
        val interestInput = findViewById<EditText>(R.id.interestRateInput)
        val periodInput = findViewById<EditText>(R.id.amortizationPeriodInput)
        val resultText = findViewById<TextView>(R.id.resultTextView)
        val calcBtn = findViewById<Button>(R.id.calculateButton)

        calcBtn.setOnClickListener {
            val amount = amountInput.text.toString().trim().toDoubleOrNull()
            val interest = interestInput.text.toString().trim().toDoubleOrNull()
            val periodYears = periodInput.text.toString().trim().toIntOrNull()

            var hasError = false
            if (amount == null) { amountInput.error = "Insert a valid amount"; hasError = true }
            if (interest == null) { interestInput.error = "Insert interest rate "; hasError = true }
            if (periodYears == null) { periodInput.error = "Insert the amortization period"; hasError = true }
            if (!hasError) {
                val monthlyPayment = calculateMonthlyPayment(amount!!, interest!!, periodYears!!)
                resultText.text = String.format("Your monthly payment is %.2f", monthlyPayment)
            }
        }
    }



}