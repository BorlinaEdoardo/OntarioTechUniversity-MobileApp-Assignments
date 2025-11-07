package com.assignment.assignment2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.assignment.assignment2.database.DatabaseHelper
import com.assignment.assignment2.database.Location

class AddLocationActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

        databaseHelper = DatabaseHelper(this)

        val addressEdit = findViewById<EditText>(R.id.editTextAddress)
        val latitudeEdit = findViewById<EditText>(R.id.editTextLatitude)
        val longitudeEdit = findViewById<EditText>(R.id.editTextLongitude)
        val saveButton = findViewById<Button>(R.id.buttonSave)
        val cancelButton = findViewById<Button>(R.id.buttonCancel)

        saveButton.setOnClickListener {
            val address = addressEdit.text.toString().trim()
            val latitudeText = latitudeEdit.text.toString().trim()
            val longitudeText = longitudeEdit.text.toString().trim()

            if (address.isEmpty() || latitudeText.isEmpty() || longitudeText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val latitude = latitudeText.toDouble()
                val longitude = longitudeText.toDouble()

                val newLocation = Location(
                    address = address,
                    latitude = latitude,
                    longitude = longitude
                )

                databaseHelper.addLocation(newLocation)
                Toast.makeText(this, "Location added successfully!", Toast.LENGTH_SHORT).show()
                finish() // Go back to main activity
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please enter valid coordinates", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            finish() // go back to main activity
        }
    }
}
