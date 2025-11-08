package com.assignment.assignment2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.assignment.assignment2.database.DatabaseHelper
import com.assignment.assignment2.database.Location
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapActivity : AppCompatActivity() {
    private lateinit var map: MapView
    private lateinit var databaseHelper: DatabaseHelper

    private var locationId: Int = -1
    private var address: String = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize OSMDroid configuration with a simple user agent
        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_map)

        databaseHelper = DatabaseHelper(this)

        // get view elements
        map = findViewById(R.id.map)
        var idTextView: TextView = findViewById(R.id.idTextView)
        var addressTextView: TextView = findViewById(R.id.addressEditText)
        var latitudeTextView: TextView = findViewById(R.id.latitudeEditText)
        var longitudeTextView: TextView = findViewById(R.id.longitudeEditText)
        var backButton: Button = findViewById(R.id.backButton)

        // Get location data from intent
        locationId = intent.getIntExtra("locationId", -1)
        address = intent.getStringExtra("address") ?: ""
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)

        // Set location details in TextViews
        idTextView.text = "Location ID: $locationId"
        addressTextView.text = address
        latitudeTextView.text = latitude.toString()
        longitudeTextView.text = longitude.toString()

        initMap()

        backButton.setOnClickListener {
            finish() // Return to previous activity
        }
    }

    private fun initMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val mapController = map.controller
        mapController.setZoom(15.0)

        if (latitude != 0.0 && longitude != 0.0) {
            val startPoint = GeoPoint(latitude, longitude)
            mapController.setCenter(startPoint)

            // Add marker for the location
            val marker = Marker(map)
            marker.position = startPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = address
            map.overlays.add(marker)
        } else {
            // Default location (Toronto)
            val defaultPoint = GeoPoint(43.6532, -79.3832)
            mapController.setCenter(defaultPoint)
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

}