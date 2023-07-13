package com.example.practicaldevang.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practicaldevang.dao.DataLocDao
import com.example.practicaldevang.data.DataLoc

@Database(entities = [DataLoc::class], version = 2, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
 
    abstract fun getLocsDao(): DataLocDao
 
    companion object {
        // Singleton prevents multiple
        // instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: LocationDatabase? = null
 
        fun getDatabase(context: Context): LocationDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "location_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}