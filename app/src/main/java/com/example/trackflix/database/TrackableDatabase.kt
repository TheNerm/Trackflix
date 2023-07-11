package com.example.trackflix.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trackflix.model.Trackable

@Database(entities = [Trackable::class], version = 2)
abstract class TrackableDatabase: RoomDatabase() {
    abstract fun trackableDao(): TrackableDao

    companion object {
        @Volatile
        private var INSTANCE: TrackableDatabase? = null

        fun getDatabase(context: Context): TrackableDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackableDatabase::class.java,
                    "trackable_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}