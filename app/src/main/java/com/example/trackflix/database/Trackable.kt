package com.example.trackflix.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trackable_table")
data class Trackable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val currentProgress: Int,
    val goal: Int,
    val type: String
)
