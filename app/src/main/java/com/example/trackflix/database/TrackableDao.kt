package com.example.trackflix.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TrackableDao {

    @Query("SELECT * FROM trackable_table")
    fun getAll(): LiveData<List<Trackable>>
    //if we need it as list, just use List<Trackable>

    @Insert
    fun insertAll(vararg trackable: Trackable)

    @Insert
    fun insertTrackable(trackable: Trackable)

    @Delete
    fun delete(trackable: Trackable)

    @Update
    fun updateUsers(vararg trackable: Trackable)

}