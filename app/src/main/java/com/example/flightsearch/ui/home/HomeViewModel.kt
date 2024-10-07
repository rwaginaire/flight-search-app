package com.example.flightsearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.FavoriteFlight
import com.example.flightsearch.data.Flight
import com.example.flightsearch.data.FlightsRepository
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val flightsRepository: FlightsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.searchText.collect { text ->
                _uiState.update { currentState ->
                    currentState.copy(
                        searchText = text,
                        searchedAirports = flightsRepository.getSearchedAirports(text)
                    )
                }
            }
        }
    }

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

    fun updateSearchText(searchedText: String){
        _uiState.update { currentState ->
            currentState.copy(
                searchText = searchedText
            )
        }
        updateSearchedAirports()
        savePreferences(searchedText)
    }

    private fun updateSearchedAirports() {
        viewModelScope.launch {
            val searchedAirports = flightsRepository.getSearchedAirports(_uiState.value.searchText)
            _uiState.update { currentState ->
                currentState.copy(
                    searchedAirports = searchedAirports
                )
            }
        }
    }

    private fun savePreferences(searchedText: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchTextPreference(searchedText)
        }
    }

    fun addOrDeleteFavoriteRoute(flight: Flight) {
        val favorite = favoritesUiState.value.favoriteFlights.firstOrNull { it.destinationAirport == flight.destinationAirport && it.departureAirport == flight.departureAirport }
        if (favorite != null)
            deleteFavoriteFlight(favorite)
        else
            addFavoriteFlight(flight)
    }

    private fun addFavoriteFlight(flight: Flight) {
        viewModelScope.launch {
            flightsRepository.insertFavorite(flight.toFavorite())
        }
    }

    fun deleteFavoriteFlight(flight: Flight) {
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