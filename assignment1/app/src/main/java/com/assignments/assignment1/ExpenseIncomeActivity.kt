package com.assignments.assignment1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseIncomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_income_layout)

        // back button to return to MainActivity
        findViewById<ImageButton>(R.id.backButtonExpenseIncome).setOnClickListener {
            finish()
        }

        val expenseInput = findViewById<EditText>(R.id.epensesEditText)
        val incomeInput = findViewById<EditText>(R.id.incomeEditText)
        val resultText = findViewById<TextView>(R.id.resultTextView)
        val calcBtn = findViewById<Button>(R.id.saveButton)

        calcBtn.setOnClickListener {
            val expense = expenseInput.text.toString().trim().toDoubleOrNull()
            val income = incomeInput.text.toString().trim().toDoubleOrNull()

            var hasError = false
            if (expense == null) {
                expenseInput.error = "Insert a valid expense value"; hasError = true
            }
            if (income == null) {
                incomeInput.error = "Insert a valid income value"; hasError = true
            }
            if (!hasError) {
                val balance = income!!- expense!!
                resultText.text = String.format("Your monthly balance without EMI is %.2f", balance)
            }
        }
    }
}