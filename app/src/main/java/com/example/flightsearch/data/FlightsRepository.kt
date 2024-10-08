package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface FlightsRepository {
    /**
     * Search airport with text search
     */
    suspend fun getSearchedAirports(text: String): List<Airport>

//    fun getSearchedAirportsStream(text: String): Flow<List<Airport>>

    /**
     * Retrieve all possible destinations from a given airport
     */
    suspend fun getAllAirports(): List<Airport>

    /**
     * Retrieve all favorites flights
     */
    suspend fun getFavorites(): List<FavoriteFlight>

    /**
     * Retrieve all favorites flights stream
     */
    fun getFavoritesStream(): Flow<List<FavoriteFlight>>

    /**
     * Get Airport from iata code
     */
    suspend fun getAirport(code: String): Airport

    /**
     * Insert favorite in the data source
     */
    suspend fun insertFavorite(favoriteFlight: FavoriteFlight)

    /**
     * Delete favorite from the data source
     */
    suspend fun deleteFavorite(favoriteFlight: FavoriteFlight)
}