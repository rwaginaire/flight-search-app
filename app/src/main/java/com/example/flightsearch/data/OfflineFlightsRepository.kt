package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineFlightsRepository(private val flightSearchDao: FlightSearchDao) : FlightsRepository {
    override suspend fun getFavorites(): List<FavoriteFlight> = flightSearchDao.getFavorites()

    override fun getFavoritesStream(): Flow<List<FavoriteFlight>> = flightSearchDao.getFavoritesStream()

    override suspend fun deleteFavorite(favoriteFlight: FavoriteFlight) = flightSearchDao.deleteFavorite(favoriteFlight)

    override suspend fun getAllAirports(): List<Airport> = flightSearchDao.getAllAirports()

    override suspend fun getSearchedAirports(text: String): List<Airport> = flightSearchDao.searchAirport(text)

//    override fun getSearchedAirportsStream(text: String): Flow<List<Airport>> = flightSearchDao.searchAirportStream(text)

    override suspend fun getAirport(code: String): Airport = flightSearchDao.getAirport(code)

    override suspend fun insertFavorite(favoriteFlight: FavoriteFlight) = flightSearchDao.insertFavorite(favoriteFlight)
}