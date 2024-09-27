package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface FlightsRepository {
    /**
     * Search airport with text search
     */
    fun getSearchedAirportsStream(text: String): Flow<List<Airport>>

    /**
     * Retrieve all possible destinations from a given airport
     */
    fun getDestinationsStream(id: Int): Flow<List<Airport>>

    /**
     * Retrieve all favorites flights
     */
    fun getFavoritesStream(): Flow<List<FavoriteFlight>>

    /**
     * Get Airport from id
     */
    fun getAirportStream(id: Int): Flow<Airport>

    /**
     * Insert favorite in the data source
     */
    suspend fun insertFavorite(favoriteFlight: FavoriteFlight)

    /**
     * Delete favorite from the data source
     */
    suspend fun deleteFavorite(favoriteFlight: FavoriteFlight)
}