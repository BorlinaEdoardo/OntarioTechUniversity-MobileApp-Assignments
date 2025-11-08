package com.assignment.assignment2.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, "locations.db", null, 2) {

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
        insertGTALocations(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Locations")
        onCreate(db)
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

    // Insert 100 Greater Toronto Area locations with real coordinates
    private fun insertGTALocations(db: SQLiteDatabase) {
        val gtaLocations = listOf(
            // Downtown Toronto
            Location(address = "CN Tower, Toronto, ON", latitude = 43.6426, longitude = -79.3871),
            Location(
                address = "Union Station, Toronto, ON",
                latitude = 43.6452,
                longitude = -79.3806
            ),
            Location(
                address = "Eaton Centre, Toronto, ON",
                latitude = 43.6544,
                longitude = -79.3807
            ),
            Location(address = "City Hall, Toronto, ON", latitude = 43.6534, longitude = -79.3839),
            Location(
                address = "Distillery District, Toronto, ON",
                latitude = 43.6503,
                longitude = -79.3597
            ),
            Location(
                address = "St. Lawrence Market, Toronto, ON",
                latitude = 43.6488,
                longitude = -79.3717
            ),
            Location(
                address = "Harbourfront Centre, Toronto, ON",
                latitude = 43.6387,
                longitude = -79.3816
            ),
            Location(
                address = "Royal Ontario Museum, Toronto, ON",
                latitude = 43.6677,
                longitude = -79.3948
            ),
            Location(address = "Casa Loma, Toronto, ON", latitude = 43.6780, longitude = -79.4094),
            Location(
                address = "Scotiabank Arena, Toronto, ON",
                latitude = 43.6434,
                longitude = -79.3790
            ),

            // North York
            Location(
                address = "North York Centre, Toronto, ON",
                latitude = 43.7615,
                longitude = -79.4111
            ),
            Location(
                address = "Yorkdale Shopping Centre, Toronto, ON",
                latitude = 43.7255,
                longitude = -79.4518
            ),
            Location(
                address = "Earl Bales Park, Toronto, ON",
                latitude = 43.7534,
                longitude = -79.4376
            ),
            Location(
                address = "Mel Lastman Square, Toronto, ON",
                latitude = 43.7731,
                longitude = -79.4126
            ),
            Location(
                address = "Don Mills Centre, Toronto, ON",
                latitude = 43.7754,
                longitude = -79.3456
            ),
            Location(
                address = "Fairview Mall, Toronto, ON",
                latitude = 43.7784,
                longitude = -79.3454
            ),
            Location(
                address = "Bayview Village Shopping Centre, Toronto, ON",
                latitude = 43.7678,
                longitude = -79.3852
            ),
            Location(
                address = "Sheppard Centre, Toronto, ON",
                latitude = 43.7616,
                longitude = -79.4111
            ),
            Location(
                address = "North York General Hospital, Toronto, ON",
                latitude = 43.7690,
                longitude = -79.4049
            ),
            Location(
                address = "York University, Toronto, ON",
                latitude = 43.7731,
                longitude = -79.5036
            ),

            // Scarborough
            Location(
                address = "Scarborough Town Centre, Scarborough, ON",
                latitude = 43.7764,
                longitude = -79.2580
            ),
            Location(
                address = "Scarborough Bluffs, Scarborough, ON",
                latitude = 43.7060,
                longitude = -79.2386
            ),
            Location(
                address = "Toronto Zoo, Scarborough, ON",
                latitude = 43.8178,
                longitude = -79.1851
            ),
            Location(
                address = "Centennial College, Scarborough, ON",
                latitude = 43.7847,
                longitude = -79.2279
            ),
            Location(
                address = "Agincourt Mall, Scarborough, ON",
                latitude = 43.7884,
                longitude = -79.2701
            ),
            Location(
                address = "Kennedy Commons, Scarborough, ON",
                latitude = 43.7274,
                longitude = -79.2646
            ),
            Location(
                address = "Cedarbrae Mall, Scarborough, ON",
                latitude = 43.7589,
                longitude = -79.2234
            ),
            Location(
                address = "Morningside Park, Scarborough, ON",
                latitude = 43.8024,
                longitude = -79.1941
            ),
            Location(
                address = "Rouge Park, Scarborough, ON",
                latitude = 43.8169,
                longitude = -79.1429
            ),
            Location(
                address = "University of Toronto Scarborough, Scarborough, ON",
                latitude = 43.7838,
                longitude = -79.1874
            ),

            // Etobicoke
            Location(
                address = "Sherway Gardens, Etobicoke, ON",
                latitude = 43.6106,
                longitude = -79.5559
            ),
            Location(
                address = "Pearson International Airport, Mississauga, ON",
                latitude = 43.6777,
                longitude = -79.6248
            ),
            Location(
                address = "Woodbine Beach, Toronto, ON",
                latitude = 43.6634,
                longitude = -79.3095
            ),
            Location(
                address = "Humber College, Etobicoke, ON",
                latitude = 43.7284,
                longitude = -79.6072
            ),
            Location(
                address = "Islington City Centre, Etobicoke, ON",
                latitude = 43.6459,
                longitude = -79.5233
            ),
            Location(
                address = "Centennial Park, Etobicoke, ON",
                latitude = 43.6439,
                longitude = -79.5851
            ),
            Location(
                address = "Cloverdale Mall, Etobicoke, ON",
                latitude = 43.6594,
                longitude = -79.5406
            ),
            Location(
                address = "Martin Grove Park, Etobicoke, ON",
                latitude = 43.6768,
                longitude = -79.5979
            ),
            Location(
                address = "Mimico Waterfront, Etobicoke, ON",
                latitude = 43.6096,
                longitude = -79.4958
            ),
            Location(
                address = "Albion Centre, Etobicoke, ON",
                latitude = 43.7426,
                longitude = -79.5478
            ),

            // Mississauga
            Location(
                address = "Square One Shopping Centre, Mississauga, ON",
                latitude = 43.5930,
                longitude = -79.6441
            ),
            Location(
                address = "Mississauga City Centre, Mississauga, ON",
                latitude = 43.5890,
                longitude = -79.6441
            ),
            Location(
                address = "Erin Mills Town Centre, Mississauga, ON",
                latitude = 43.5575,
                longitude = -79.7332
            ),
            Location(
                address = "Heartland Town Centre, Mississauga, ON",
                latitude = 43.6539,
                longitude = -79.7391
            ),
            Location(
                address = "Port Credit, Mississauga, ON",
                latitude = 43.5497,
                longitude = -79.5912
            ),
            Location(
                address = "Sheridan College, Mississauga, ON",
                latitude = 43.5890,
                longitude = -79.6516
            ),
            Location(
                address = "Credit Valley Hospital, Mississauga, ON",
                latitude = 43.5896,
                longitude = -79.6516
            ),
            Location(
                address = "Lakefront Promenade, Mississauga, ON",
                latitude = 43.5618,
                longitude = -79.5943
            ),
            Location(
                address = "Meadowvale Town Centre, Mississauga, ON",
                latitude = 43.6007,
                longitude = -79.7567
            ),
            Location(
                address = "Dixie Outlet Mall, Mississauga, ON",
                latitude = 43.6191,
                longitude = -79.6985
            ),

            // Brampton
            Location(
                address = "Bramalea City Centre, Brampton, ON",
                latitude = 43.7310,
                longitude = -79.7624
            ),
            Location(
                address = "Trinity Common Mall, Brampton, ON",
                latitude = 43.7059,
                longitude = -79.7598
            ),
            Location(
                address = "Sheridan College Brampton, Brampton, ON",
                latitude = 43.7315,
                longitude = -79.7624
            ),
            Location(
                address = "Brampton City Hall, Brampton, ON",
                latitude = 43.6837,
                longitude = -79.7597
            ),
            Location(address = "Gage Park, Brampton, ON", latitude = 43.6890, longitude = -79.7669),
            Location(
                address = "Heart Lake Conservation Area, Brampton, ON",
                latitude = 43.7486,
                longitude = -79.8021
            ),
            Location(
                address = "Chinguacousy Park, Brampton, ON",
                latitude = 43.6890,
                longitude = -79.7200
            ),
            Location(
                address = "Shoppers World Brampton, Brampton, ON",
                latitude = 43.6827,
                longitude = -79.7597
            ),
            Location(
                address = "Professor's Lake, Brampton, ON",
                latitude = 43.7486,
                longitude = -79.8021
            ),
            Location(
                address = "Brampton Civic Hospital, Brampton, ON",
                latitude = 43.7315,
                longitude = -79.7624
            ),

            // Markham
            Location(
                address = "Markville Shopping Centre, Markham, ON",
                latitude = 43.8435,
                longitude = -79.3097
            ),
            Location(
                address = "Pacific Mall, Markham, ON",
                latitude = 43.8277,
                longitude = -79.3097
            ),
            Location(
                address = "First Markham Place, Markham, ON",
                latitude = 43.8561,
                longitude = -79.3370
            ),
            Location(
                address = "Unionville Main Street, Markham, ON",
                latitude = 43.8661,
                longitude = -79.3114
            ),
            Location(
                address = "Markham Civic Centre, Markham, ON",
                latitude = 43.8561,
                longitude = -79.3370
            ),
            Location(
                address = "Toogood Pond Park, Markham, ON",
                latitude = 43.8561,
                longitude = -79.3370
            ),
            Location(
                address = "Seneca College Markham, Markham, ON",
                latitude = 43.8561,
                longitude = -79.3370
            ),
            Location(
                address = "Milliken Mills Community Centre, Markham, ON",
                latitude = 43.8386,
                longitude = -79.2907
            ),
            Location(
                address = "Cornell Community Centre, Markham, ON",
                latitude = 43.8561,
                longitude = -79.3370
            ),
            Location(
                address = "Angus Glen Golf Club, Markham, ON",
                latitude = 43.8561,
                longitude = -79.3370
            ),

            // Vaughan
            Location(
                address = "Vaughan Mills, Vaughan, ON",
                latitude = 43.8257,
                longitude = -79.5391
            ),
            Location(
                address = "Canada's Wonderland, Vaughan, ON",
                latitude = 43.8430,
                longitude = -79.5390
            ),
            Location(
                address = "Vaughan Metropolitan Centre, Vaughan, ON",
                latitude = 43.8372,
                longitude = -79.5083
            ),
            Location(
                address = "Colossus Centre, Vaughan, ON",
                latitude = 43.8372,
                longitude = -79.5083
            ),
            Location(
                address = "Promenade Mall, Vaughan, ON",
                latitude = 43.8372,
                longitude = -79.5083
            ),
            Location(
                address = "Vaughan City Hall, Vaughan, ON",
                latitude = 43.8372,
                longitude = -79.5083
            ),
            Location(
                address = "Boyd Conservation Park, Vaughan, ON",
                latitude = 43.8700,
                longitude = -79.5800
            ),
            Location(
                address = "Maple Community Centre, Vaughan, ON",
                latitude = 43.8563,
                longitude = -79.4976
            ),
            Location(
                address = "Kortright Centre, Vaughan, ON",
                latitude = 43.8700,
                longitude = -79.5800
            ),
            Location(
                address = "McMichael Canadian Art Collection, Vaughan, ON",
                latitude = 43.8700,
                longitude = -79.5800
            ),

            // Richmond Hill
            Location(
                address = "Hillcrest Mall, Richmond Hill, ON",
                latitude = 43.8828,
                longitude = -79.4403
            ),
            Location(
                address = "Richmond Hill Centre, Richmond Hill, ON",
                latitude = 43.8828,
                longitude = -79.4403
            ),
            Location(
                address = "David Dunlap Observatory, Richmond Hill, ON",
                latitude = 43.8828,
                longitude = -79.4403
            ),
            Location(
                address = "Richmond Hill Golf Club, Richmond Hill, ON",
                latitude = 43.8828,
                longitude = -79.4403
            ),
            Location(
                address = "Oak Ridges Moraine, Richmond Hill, ON",
                latitude = 43.9500,
                longitude = -79.4000
            ),
            Location(
                address = "Richmond Green Sports Centre, Richmond Hill, ON",
                latitude = 43.8828,
                longitude = -79.4403
            ),
            Location(
                address = "Mill Pond Park, Richmond Hill, ON",
                latitude = 43.8828,
                longitude = -79.4403
            ),
            Location(
                address = "Richmond Hill Public Library, Richmond Hill, ON",
                latitude = 43.8828,
                longitude = -79.4403
            ),
            Location(
                address = "Crosby Arena, Richmond Hill, ON",
                latitude = 43.8828,
                longitude = -79.4403
            ),
            Location(
                address = "Richmond Hill Heritage Centre, Richmond Hill, ON",
                latitude = 43.8828,
                longitude = -79.4403
            ),

            // Oakville
            Location(
                address = "Oakville Place, Oakville, ON",
                latitude = 43.4675,
                longitude = -79.6877
            ),
            Location(
                address = "Sheridan College Oakville, Oakville, ON",
                latitude = 43.4675,
                longitude = -79.6877
            ),
            Location(
                address = "Oakville Hospital, Oakville, ON",
                latitude = 43.4675,
                longitude = -79.6877
            ),
            Location(
                address = "Lakeside Park, Oakville, ON",
                latitude = 43.4418,
                longitude = -79.6707
            ),
            Location(
                address = "Glen Abbey Golf Club, Oakville, ON",
                latitude = 43.4478,
                longitude = -79.7292
            ),
            Location(
                address = "Sixteen Mile Sports Complex, Oakville, ON",
                latitude = 43.4675,
                longitude = -79.6877
            ),
            Location(
                address = "Oakville Centre for the Performing Arts, Oakville, ON",
                latitude = 43.4675,
                longitude = -79.6877
            ),
            Location(
                address = "Bronte Creek Provincial Park, Oakville, ON",
                latitude = 43.4000,
                longitude = -79.7500
            ),
            Location(
                address = "Coronation Park, Oakville, ON",
                latitude = 43.4418,
                longitude = -79.6707
            ),
            Location(
                address = "Appleby College, Oakville, ON",
                latitude = 43.4675,
                longitude = -79.6877
            ),

            // Burlington
            Location(
                address = "Mapleview Centre, Burlington, ON",
                latitude = 43.3255,
                longitude = -79.7990
            ),
            Location(
                address = "Burlington Mall, Burlington, ON",
                latitude = 43.3255,
                longitude = -79.7990
            ),
            Location(
                address = "Royal Botanical Gardens, Burlington, ON",
                latitude = 43.2969,
                longitude = -79.8711
            ),
            Location(
                address = "Spencer Smith Park, Burlington, ON",
                latitude = 43.3096,
                longitude = -79.7885
            ),
            Location(
                address = "Burlington Beach, Burlington, ON",
                latitude = 43.3096,
                longitude = -79.7885
            ),
            Location(
                address = "Joseph Brant Hospital, Burlington, ON",
                latitude = 43.3255,
                longitude = -79.7990
            ),
            Location(
                address = "Aldershot GO Station, Burlington, ON",
                latitude = 43.2969,
                longitude = -79.8711
            ),
            Location(
                address = "Mount Nemo Conservation Area, Burlington, ON",
                latitude = 43.3700,
                longitude = -79.9700
            ),
            Location(
                address = "Burlington Performing Arts Centre, Burlington, ON",
                latitude = 43.3255,
                longitude = -79.7990
            ),
            Location(
                address = "Rattlesnake Point Conservation Area, Burlington, ON",
                latitude = 43.3700,
                longitude = -79.9700
            ),

            // Ajax
            Location(address = "Durham Centre, Ajax, ON", latitude = 43.8503, longitude = -79.0204),
            Location(
                address = "Ajax Community Centre, Ajax, ON",
                latitude = 43.8503,
                longitude = -79.0204
            ),
            Location(address = "Rotary Park, Ajax, ON", latitude = 43.8503, longitude = -79.0204),
            Location(
                address = "Ajax GO Station, Ajax, ON",
                latitude = 43.8503,
                longitude = -79.0204
            ),
            Location(
                address = "Greenwood Conservation Area, Ajax, ON",
                latitude = 43.8700,
                longitude = -79.0000
            ),

            // Pickering
            Location(
                address = "Pickering Town Centre, Pickering, ON",
                latitude = 43.8354,
                longitude = -79.0868
            ),
            Location(
                address = "Pickering Nuclear Station, Pickering, ON",
                latitude = 43.8108,
                longitude = -79.0653
            ),
            Location(
                address = "Frenchman's Bay, Pickering, ON",
                latitude = 43.8354,
                longitude = -79.0868
            ),
            Location(
                address = "Petticoat Creek Conservation Area, Pickering, ON",
                latitude = 43.8354,
                longitude = -79.0868
            ),
            Location(
                address = "Pickering Museum Village, Pickering, ON",
                latitude = 43.8354,
                longitude = -79.0868
            ),

            // Oshawa
            Location(
                address = "Oshawa Centre, Oshawa, ON",
                latitude = 43.8971,
                longitude = -78.8658
            ),
            Location(
                address = "University of Ontario Institute of Technology, Oshawa, ON",
                latitude = 43.9453,
                longitude = -78.8960
            ),
            Location(
                address = "Lakeview Park, Oshawa, ON",
                latitude = 43.8689,
                longitude = -78.8658
            ),
            Location(
                address = "Canadian Automotive Museum, Oshawa, ON",
                latitude = 43.8971,
                longitude = -78.8658
            ),
            Location(
                address = "Oshawa General Hospital, Oshawa, ON",
                latitude = 43.8971,
                longitude = -78.8658
            )
        )

        for (location in gtaLocations) {
            val values = location.toContentValues()
            db.insert("Locations", null, values)
        }
        }
    }
