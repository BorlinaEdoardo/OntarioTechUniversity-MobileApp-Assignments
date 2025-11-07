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


    // Get Location by ID

    // Add location

    // Update location

    // Delete location


}