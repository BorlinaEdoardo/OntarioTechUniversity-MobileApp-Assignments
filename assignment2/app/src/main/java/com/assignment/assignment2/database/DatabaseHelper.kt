package com.assignment.assignment2.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, "locations.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Locations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                address TEXT NOT NULL,
                latitude REAL NOT NULL,
                longitude REAL NOT NULL
            )
            """
        )
        insertSampleData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Locations")
        onCreate(db)
    }

    // Insert sample data using the provided database instance
    private fun insertSampleData(db: SQLiteDatabase) {
        val sampleLocations = listOf(
            Location(address = "Toronto, ON", latitude = 43.65107, longitude = -79.347015),
            Location(address = "Mississauga, ON", latitude = 43.589045, longitude = -79.644119),
            Location(address = "Brampton, ON", latitude = 43.731548, longitude = -79.762417),
            Location(address = "Markham, ON", latitude = 43.8561, longitude = -79.337),
            Location(address = "Vaughan, ON", latitude = 43.8372, longitude = -79.5083),
            Location(address = "Richmond Hill, ON", latitude = 43.8828, longitude = -79.4403),
            Location(address = "Oakville, ON", latitude = 43.4675, longitude = -79.6877),
            Location(address = "Burlington, ON", latitude = 43.3255, longitude = -79.799),
            Location(address = "Milton, ON", latitude = 43.5186, longitude = -79.8774),
            Location(address = "Ajax, ON", latitude = 43.8503, longitude = -79.0204)
        )

        for (location in sampleLocations) {
            val values = location.toContentValues()
            db.insert("Locations", null, values)
        }
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

    // Get Location by Address
    fun getLocationByAddress(address: String): Location? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Locations WHERE address = ?", arrayOf(address))
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
    fun updateLocation(location: Location) {
        val db = writableDatabase
        val values = location.toContentValues()
        db.update("Locations", values, "id = ?", arrayOf(location.id.toString()))
    }

    // Delete location
    fun deleteLocation(id: Int) {
        val db = writableDatabase
        db.delete("Locations", "id = ?", arrayOf(id.toString()))
    }


}