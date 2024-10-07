package com.example.flightsearch.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Flight
import com.example.flightsearch.data.FlightsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val flightsRepository: FlightsRepository
): ViewModel() {

    private val airportId: Int = checkNotNull(savedStateHandle[SearchDestination.airportIdArg])

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        // Initialize view model from airportId
        viewModelScope.launch {
            val allAirports = flightsRepository.getAllAirports()
            val departureAirport = allAirports.first { it.id == airportId }
            val flights = getPossibleFlights(departureAirport, allAirports)
            _uiState.update { currentState ->
                currentState.copy(
                    departureAirport = departureAirport,
                    flightsList = flights,
                )
            }
        }
    }

    private fun getPossibleFlights(departureAirport: Airport, allAirports: List<Airport>): List<Flight> {
        val flights = allAirports
            .filter { it.id != departureAirport.id }
            .map {
                Flight(
                    departureAirport = departureAirport,
                    destinationAirport = it
                )
            }
        return flights
    }
}