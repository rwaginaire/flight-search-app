package com.example.flightsearch.ui.home

import com.example.flightsearch.data.Airport

data class HomeUiState(
    val searchText: String = "",
    val searchedAirports: List<Airport> = listOf()
)
