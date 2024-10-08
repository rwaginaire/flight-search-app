package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightSearchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favoriteFlight: FavoriteFlight)

    @Delete
    suspend fun deleteFavorite(favoriteFlight: FavoriteFlight)

    @Query(
        """
        SELECT * FROM airport 
        WHERE iata_code like '%' || :searchText || '%'
        OR name like '%' || :searchText || '%'
        ORDER BY passengers DESC
        """
    )
    suspend fun searchAirport(searchText: String): List<Airport>

//    @Query(
//        """
//        SELECT * FROM airport
//        WHERE iata_code like '%' || :searchText || '%'
//        OR name like '%' || :searchText || '%'
//        """
//    )
//    fun searchAirportStream(searchText: String): Flow<List<Airport>>

    @Query(
        """
        SELECT * FROM airport
        """
    )
    suspend fun getAllAirports(): List<Airport>

    @Query("SELECT * from favorite")
    suspend fun getFavorites(): List<FavoriteFlight>

    @Query("SELECT * from favorite")
    fun getFavoritesStream(): Flow<List<FavoriteFlight>>

    @Query("SELECT * from airport WHERE iata_code = :code")
    suspend fun getAirport(code: String): Airport
}