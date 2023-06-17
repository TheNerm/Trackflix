package com.example.trackflix.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackflix.model.Trackable

@Dao
interface TrackableDao {

    @Query("SELECT * FROM trackable_table")
    fun getAll(): LiveData<List<Trackable>>
    //if we need it as list, just use List<Trackable>

    @Insert
    suspend fun insertAllTrackables(vararg trackable: Trackable)

    @Insert
    suspend fun insertTrackable(trackable: Trackable)

    @Delete
    suspend fun deleteTrackable(trackable: Trackable)

    @Query("DELETE  FROM trackable_table")
    suspend fun deleteAllTrackables()

    @Update
    suspend fun updateTrackable(vararg trackable: Trackable)

}