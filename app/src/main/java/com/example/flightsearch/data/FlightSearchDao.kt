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
        """
    )
    fun searchAirport(searchText: String): Flow<List<Airport>>

    @Query(
        """
        SELECT * FROM airport 
        WHERE id <> :id
        """
    )
    fun getDestinations(id: Int): Flow<List<Airport>>

    @Query("SELECT * from favorite")
    fun getFavorites(): Flow<List<FavoriteFlight>>

    @Query("SELECT * from airport WHERE id = :id")
    fun getAirport(id: Int): Flow<Airport>
}