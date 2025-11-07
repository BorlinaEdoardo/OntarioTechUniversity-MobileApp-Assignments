package com.assignment.assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity() {
    private var address: String = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        address = intent.getStringExtra("address") ?: ""
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
    }

}