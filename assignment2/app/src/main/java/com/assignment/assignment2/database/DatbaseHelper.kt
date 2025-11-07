package com.assignment.assignment2.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper

class DatbaseHelper(context: Context): SQLiteOpenHelper(context, "locations.db", null, 1) {
    override fun onCreate(db: android.database.sqlite.SQLiteDatabase?) {
        db?.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Locations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                address TEXT NOT NULL,
                latitude REAL NOT NULL,
                longitude REAL NOT NULL
            )
            """
        )

    }


    override fun onUpgrade(db: android.database.sqlite.SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        // Delete and recreate the table
        db?.execSQL("DROP TABLE IF EXISTS Locations")
        onCreate(db!!)
    }

    /// CRUD operations

    // Get all locations
    fun getAllLocations(): List<Location> {
        val locations = mutableListOf<Location>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Locations", null)
        while (cursor.moveToNext()) {
            val location = Location.getFromCursor(cursor)
            locations.add(location)
        }
        cursor.close()

        return locations
    }

    // Get Location by ID
    fun getLocationById(id: Int): Location? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Locations WHERE id = ?", arrayOf(id.toString()))
        var location: Location? = null
        if (cursor.moveToFirst()) {
            location = Location.getFromCursor(cursor)
        }
        cursor.close()
        return location
    }

    // Add location
    fun addLocation(location: Location) {
        val db = writableDatabase
        val values = location.toContentValues()
        db.insert("Locations", null, values)
    }

    // Update location

    // Delete location


}