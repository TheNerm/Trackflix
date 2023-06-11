package com.example.trackflix.repository

import androidx.lifecycle.LiveData
import com.example.trackflix.database.TrackableDao
import com.example.trackflix.model.Trackable

class TrackableRepository(private val trackableDao: TrackableDao) {

    val readAllData: LiveData<List<Trackable>> = trackableDao.getAll()

    suspend fun addTrackable(trackable: Trackable){
        trackableDao.insertTrackable(trackable)
    }

    suspend fun updateTrackable (trackable: Trackable){
        trackableDao.updateTrackable(trackable)
    }

}