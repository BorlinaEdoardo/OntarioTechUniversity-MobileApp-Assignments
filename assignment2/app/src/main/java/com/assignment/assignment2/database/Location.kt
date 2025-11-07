package com.assignment.assignment2.database

data class Location (
    val id: Int? = null,
    val address: String,
    val latitude: Double,
    val longitude: Double
) {

    fun toContentValues(): android.content.ContentValues {
        return android.content.ContentValues().apply {
            put("address", address)
            put("latitude", latitude)
            put("longitude", longitude)
        }
    }

    companion object {
        fun getFromCursor(cursor: android.database.Cursor): Location {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
            val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"))
            val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))

            return Location(id, address, latitude, longitude)
        }
    }
}