package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.FlightsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FlightSearchViewModel(
    private val flightsRepository: FlightsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(FlightSearchUiState())
    val uiState: StateFlow<FlightSearchUiState> = _uiState.asStateFlow()

    fun updateSearchText(searchedText: String){
        _uiState.update { currentState ->
            currentState.copy(
                searchText = searchedText
            )
        }
    }

    fun updateSelectedAirport(airport: Airport) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedAirportDetails = airport.toAirportDetails()
            )
        }
    }

    fun searchAirportsWithText(inputText: String): Flow<List<Airport>> {
        return flightsRepository.getSearchedAirportsStream(inputText)
    }

    fun getPossibleDestinationsFromAirport(airportId: Int): Flow<List<Airport>> {
        return flightsRepository.getDestinationsStream(airportId)
    }

    fun addFavoriteRoute() {
        
    }
}

data class AirportDetails(
    val id: Int = 0,
    val iataCode: String = "",
    val name: String = ""
)

data class FlightSearchUiState(
    val searchText: String = "",
    val selectedAirportDetails: AirportDetails = AirportDetails(),
)

/**
 * Extension function to convert [Airport] to [AirportDetails]
 */
fun Airport.toAirportDetails(): AirportDetails = AirportDetails(
    id = id,
    name = name,
    iataCode = iataCode
)

/**
 * Extension function to convert [AirportDetails] to [Airport]
 */
fun AirportDetails.toAirport(): Airport = Airport(
    id = id,
    name = name,
    iataCode = iataCode,
    passengers = 0
)