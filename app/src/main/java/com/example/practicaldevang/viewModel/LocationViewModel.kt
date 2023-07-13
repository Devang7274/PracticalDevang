package com.example.practicaldevang.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.practicaldevang.data.DataLoc
import com.example.practicaldevang.database.LocationDatabase
import com.example.practicaldevang.repo.LocRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    val allLocations: LiveData<List<DataLoc>>
    val repository: LocRepository

    init {
        val dao = LocationDatabase.getDatabase(application).getLocsDao()
        repository = LocRepository(dao)
        allLocations = repository.allLocations
    }

    fun deleteLocationData(dataLoc: DataLoc) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(dataLoc)
    }

    fun updateLocationData(dataLoc: DataLoc) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(dataLoc)
    }

    fun addLocationData(dataLoc: DataLoc) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(dataLoc)
    }
}