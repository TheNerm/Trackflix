package com.example.trackflix.database

import androidx.lifecycle.LiveData

class TrackableRepository(private val trackableDao: TrackableDao) {

    val readAllData: LiveData<List<Trackable>> = trackableDao.getAll()

    suspend fun addTrackable(trackable: Trackable){
        trackableDao.insertTrackable(trackable)
    }

}