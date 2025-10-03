package com.assignments.assignment1

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class BalanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)

        // back button to return to MainActivity
        findViewById<ImageButton>(R.id.backButtonBalance).setOnClickListener {
            finish()
        }

        val incomeInput = findViewById<EditText>(R.id.incomeEditText)
        val expensesInput = findViewById<EditText>(R.id.expensesEditText)
        val emiInput = findViewById<EditText>(R.id.emiEditText)
        val resultText = findViewById<TextView>(R.id.resultTextView)
        val calcBtn = findViewById<Button>(R.id.calculateBalanceButton)
        val resetBtn = findViewById<Button>(R.id.resetButton)

        // Load saved data
        loadSavedData()

        // Reset button listener to clear all preferences and input fields
        resetBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("financial_data", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            // clear all the values
            incomeInput.setText("")
            expensesInput.setText("")
            emiInput.setText("")

            resultText.text = ""

            incomeInput.error = null
            expensesInput.error = null
            emiInput.error = null
        }

        calcBtn.setOnClickListener {
            val income = incomeInput.text.toString().trim().toDoubleOrNull()
            val expenses = expensesInput.text.toString().trim().toDoubleOrNull()
            val emi = emiInput.text.toString().trim().toDoubleOrNull()

            var hasError = false
            if (income == null) {
                incomeInput.error = "Insert a valid income value"
                hasError = true
            }
            if (expenses == null) {
                expensesInput.error = "Insert a valid expenses value"
                hasError = true
            }
            if (emi == null) {
                emiInput.error = "Insert a valid EMI value"
                hasError = true
            }

            if (!hasError) {
                val finalBalance = income!! - expenses!! - emi!!

                if (finalBalance > 0) {
                    resultText.text = String.format("POSITIVE BALANCE: +%.2f", finalBalance)
                    resultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
                } else if (finalBalance < 0) {
                    resultText.text = String.format(" NEGATIVE BALANCE: %.2f", finalBalance)
                    resultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                } else {
                    resultText.text = "BALANCED: 0.00"
                    resultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
                }
            }
        }
    }

    private fun loadSavedData() {
        val incomeInput = findViewById<EditText>(R.id.incomeEditText)
        val expensesInput = findViewById<EditText>(R.id.expensesEditText)
        val emiInput = findViewById<EditText>(R.id.emiEditText)

        val sharedPreferences = getSharedPreferences("financial_data", Context.MODE_PRIVATE)

        // income
        val savedIncome = sharedPreferences.getFloat("monthly_income", 0.0f).toDouble()
        if (savedIncome > 0) {
            incomeInput.setText(savedIncome.toString())
        }

        // expenses
        val savedExpenses = sharedPreferences.getFloat("monthly_expenses", 0.0f).toDouble()
        if (savedExpenses > 0) {
            expensesInput.setText(savedExpenses.toString())
        }

        // EMI
        val savedEMI = sharedPreferences.getFloat("monthly_emi", 0.0f).toDouble()
        if (savedEMI > 0) {
            emiInput.setText(savedEMI.toString())
        }

        // calculate (if all values are present)
        if (savedIncome > 0 && savedExpenses > 0 && savedEMI > 0) {
            findViewById<Button>(R.id.calculateBalanceButton).performClick()
        }
    }
}
