package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineFlightsRepository(private val flightSearchDao: FlightSearchDao) : FlightsRepository {
    override fun getFavoritesStream(): Flow<List<FavoriteFlight>> = flightSearchDao.getFavorites()

    override suspend fun deleteFavorite(favoriteFlight: FavoriteFlight) = flightSearchDao.deleteFavorite(favoriteFlight)

    override fun getDestinationsStream(id: Int): Flow<List<Airport>> = flightSearchDao.getDestinations(id)

    override fun getSearchedAirportsStream(text: String): Flow<List<Airport>> = flightSearchDao.searchAirport(text)

    override fun getAirportStream(id: Int): Flow<Airport> = flightSearchDao.getAirport(id)

    override suspend fun insertFavorite(favoriteFlight: FavoriteFlight) = flightSearchDao.insertFavorite(favoriteFlight)
}