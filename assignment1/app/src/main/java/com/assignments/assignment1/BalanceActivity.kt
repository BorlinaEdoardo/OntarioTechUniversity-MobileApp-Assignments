package com.assignments.assignment1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class BalanceActivity : AppCompatActivity() {
    private lateinit var dataManager: FinancialDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)

        // Initialize data manager
        dataManager = FinancialDataManager(this)

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

        // Load saved data automatically
        loadSavedData()

        // Reset button listener to clear all preferences and input fields
        resetBtn.setOnClickListener {
            // Clear all SharedPreferences data
            dataManager.clearAllData()

            // Clear all input fields
            incomeInput.setText("")
            expensesInput.setText("")
            emiInput.setText("")

            // Clear result text
            resultText.text = ""

            // Clear any existing errors
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

        // Load saved income
        val savedIncome = dataManager.getMonthlyIncome()
        if (savedIncome > 0) {
            incomeInput.setText(savedIncome.toString())
        }

        // Load saved expenses
        val savedExpenses = dataManager.getMonthlyExpenses()
        if (savedExpenses > 0) {
            expensesInput.setText(savedExpenses.toString())
        }

        // Load saved EMI
        val savedEMI = dataManager.getMonthlyEMI()
        if (savedEMI > 0) {
            emiInput.setText(savedEMI.toString())
        }

        // If all data is available, calculate automatically
        if (dataManager.hasAllData()) {
            findViewById<Button>(R.id.calculateBalanceButton).performClick()
        }
    }
}
