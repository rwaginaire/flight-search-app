package com.example.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteFlight::class, Airport::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun flightSearchDao(): FlightSearchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .createFromAsset("database/flight_search.db")
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}