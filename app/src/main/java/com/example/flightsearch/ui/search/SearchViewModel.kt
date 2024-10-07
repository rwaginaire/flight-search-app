package com.example.flightsearch.ui.search

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.FavoriteFlight
import com.example.flightsearch.data.Flight
import com.example.flightsearch.data.FlightsRepository
import com.example.flightsearch.ui.home.FavoritesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val flightsRepository: FlightsRepository
): ViewModel() {

    private val airportId: Int = checkNotNull(savedStateHandle[SearchDestination.airportIdArg])

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val favoritesUiState: StateFlow<FavoritesUiState> =
        flightsRepository.getFavoritesStream().map { favoriteFlights ->
            FavoritesUiState(
                favoriteFlights = favoriteFlights.map { favoriteFlight ->
                    Flight(
                        favoriteFlight.id,
                        flightsRepository.getAirport(favoriteFlight.departureCode),
                        destinationAirport = flightsRepository.getAirport(favoriteFlight.destinationCode),
                        isFavorite = true
                    )
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = FavoritesUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

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

    fun addOrDeleteFavoriteRoute(flight: Flight) {
        val favorite = favoritesUiState.value.favoriteFlights.firstOrNull { it.destinationAirport == flight.destinationAirport && it.departureAirport == flight.departureAirport }
        if (favorite != null)
            deleteFavoriteRoute(favorite)
        else
            addFavoriteRoute(flight)
    }

    private fun addFavoriteRoute(flight: Flight) {
        viewModelScope.launch {
            flightsRepository.insertFavorite(flight.toFavorite())
        }
    }

    private fun deleteFavoriteRoute(flight: Flight) {
        viewModelScope.launch {
            flightsRepository.deleteFavorite(flight.toFavorite())
        }
    }
}

/**
 * Extension function to convert [Flight] to [FavoriteFlight]
 */
fun Flight.toFavorite(): FavoriteFlight = FavoriteFlight(
    departureCode = departureAirport.iataCode,
    destinationCode = destinationAirport.iataCode,
    id = id
)