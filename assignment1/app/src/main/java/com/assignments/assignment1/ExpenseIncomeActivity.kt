package com.assignments.assignment1

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ExpenseIncomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_income_layout)

        // back button to return to MainActivity
        findViewById<ImageButton>(R.id.backButtonExpenseIncome).setOnClickListener {
            finish()
        }

    }
}