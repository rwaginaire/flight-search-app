package com.example.flightsearch.data

object MockData {
    val airports = listOf(
        Airport(
            id = 1,
            iataCode = "OPO",
            name = "Francisco Sá Carne Airport",
            passengers = 5053134,
        ),
        Airport(
            id = 2,
            iataCode = "SAA",
            name = "Stockholm land Airport",
            passengers = 7494765,
        ),
        Airport(
            id = 3,
            iataCode = "WAW",
            name = "Warsaw Chopin Airport",
            passengers = 18860000,
        ),
        Airport(
            id = 4,
            iataCode = "MRS",
            name = "Marseille Provence Airport",
            passengers = 10151743,
        ),
        Airport(
            id = 5,
            name = "Milan Berg Airport",
            passengers = 3833063,
        )
    )

    val flights = airports
        .filter { it.id > 0 }
        .map {
            Flight(
                departureAirport = airports[0],
                destinationAirport = it
            )
        }
}