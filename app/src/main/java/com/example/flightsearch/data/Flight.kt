package com.example.flightsearch.data

data class Flight(
    val id: Int = 0,
    val departureAirport: Airport = Airport(),
    val destinationAirport: Airport = Airport(),
    var isFavorite: Boolean = false
)
