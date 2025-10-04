package com.assignments.assignment1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        // navigate to EmiCalcActivity on button click
        findViewById<Button>(R.id.button3).setOnClickListener {
            startActivity(Intent(this, EmiCalcActivity::class.java))
        }

        // navigate to ExpenseIncomeActivity on button click
        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, ExpenseIncomeActivity::class.java))
        }

        // navigate to BalanceActivity on button click
        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, BalanceActivity::class.java))
        }
    }

}