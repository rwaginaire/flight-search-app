package com.example.flightsearch.ui.home

import com.example.flightsearch.data.Flight

data class FavoritesUiState(
    val favoriteFlights: List<Flight> = listOf()
)
