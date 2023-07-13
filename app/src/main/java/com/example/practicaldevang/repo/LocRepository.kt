package com.example.practicaldevang.repo

import androidx.lifecycle.LiveData
import com.example.practicaldevang.dao.DataLocDao
import com.example.practicaldevang.data.DataLoc

class LocRepository(private val locDao: DataLocDao) {

    val allLocations: LiveData<List<DataLoc>> = locDao.getAllLocations()

    suspend fun insert(dataLoc: DataLoc) {
        locDao.insert(dataLoc)
    }
     
    suspend fun delete(dataLoc: DataLoc){
        locDao.delete(dataLoc)
    }


    suspend fun update(dataLoc: DataLoc){
         locDao.update(dataLoc)
    }
}