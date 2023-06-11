package com.example.trackflix.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "trackable_table")
data class Trackable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val currentProgress: Int,
    val goal: Int,
    val type: String
): Parcelable
