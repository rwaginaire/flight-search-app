package com.example.flightsearch.ui.search

import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Flight

data class SearchUiState(
    val departureAirport: Airport = Airport(),
    val flightsList: List<Flight> = listOf(),
)
