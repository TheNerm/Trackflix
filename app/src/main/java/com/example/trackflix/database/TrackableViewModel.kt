package com.example.trackflix.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackableViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Trackable>>
    private val repository: TrackableRepository

    init{
        val trackableDao = TrackableDatabase.getDatabase(application).trackableDao()
        repository = TrackableRepository(trackableDao)
        readAllData = repository.readAllData
    }

    fun addTrackable(trackable: Trackable){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTrackable(trackable)
        }
    }



}