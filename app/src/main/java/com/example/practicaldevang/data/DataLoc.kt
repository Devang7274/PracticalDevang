package com.example.practicaldevang.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationTable")

class DataLoc(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "lat") val lat: String,
    @ColumnInfo(name = "lon") val lon: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}