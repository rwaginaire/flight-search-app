package com.example.flightsearch.ui.home

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Flight
import com.example.flightsearch.data.FlightsRepository
import com.example.flightsearch.ui.search.SearchUiState
import com.example.flightsearch.ui.search.toFavorite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val flightsRepository: FlightsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

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

    fun deleteFavoriteFlight(flight: Flight) {
        viewModelScope.launch {
            flightsRepository.deleteFavorite(flight.toFavorite())
        }
    }
}