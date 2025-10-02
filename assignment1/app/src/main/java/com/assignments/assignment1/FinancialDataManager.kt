package com.assignments.assignment1

import android.content.Context
import android.content.SharedPreferences

class FinancialDataManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("financial_data", Context.MODE_PRIVATE)

    // Save monthly income
    fun saveMonthlyIncome(income: Double) {
        sharedPreferences.edit().putFloat("monthly_income", income.toFloat()).apply()
    }

    // Get monthly income
    fun getMonthlyIncome(): Double {
        return sharedPreferences.getFloat("monthly_income", 0.0f).toDouble()
    }

    // Save monthly expenses
    fun saveMonthlyExpenses(expenses: Double) {
        sharedPreferences.edit().putFloat("monthly_expenses", expenses.toFloat()).apply()
    }

    // Get monthly expenses
    fun getMonthlyExpenses(): Double {
        return sharedPreferences.getFloat("monthly_expenses", 0.0f).toDouble()
    }

    // Save monthly EMI
    fun saveMonthlyEMI(emi: Double) {
        sharedPreferences.edit().putFloat("monthly_emi", emi.toFloat()).apply()
    }

    // Get monthly EMI
    fun getMonthlyEMI(): Double {
        return sharedPreferences.getFloat("monthly_emi", 0.0f).toDouble()
    }

    // Check if all data is available
    fun hasAllData(): Boolean {
        return getMonthlyIncome() > 0 && getMonthlyExpenses() > 0 && getMonthlyEMI() > 0
    }

    // Clear all data
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }
}
