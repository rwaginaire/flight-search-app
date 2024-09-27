package com.example.flightsearch.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val flightsRepository: FlightsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineFlightsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [FlightsRepository]
     */
    override val flightsRepository: FlightsRepository by lazy {
        OfflineFlightsRepository(AppDatabase.getDatabase(context).flightSearchDao())
    }
}