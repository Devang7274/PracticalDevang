package com.example.practicaldevang.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.practicaldevang.data.DataLoc

@Dao
interface DataLocDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dataLoc: DataLoc)

    @Delete
    suspend fun delete(dataLoc: DataLoc)

    @Query("Select * from locationTable order by id ASC")
    fun getAllLocations(): LiveData<List<DataLoc>>

    @Update
    suspend fun update(dataLoc: DataLoc)
}