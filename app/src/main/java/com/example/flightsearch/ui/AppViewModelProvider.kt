package com.example.flightsearch.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.ui.home.HomeViewModel
import com.example.flightsearch.ui.search.SearchViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            val application = flightSearchApplication()
            HomeViewModel(
                application.container.flightsRepository,
                application.userPreferencesRepository
            )
        }

        // Initializer for SearchViewModel
        initializer {
            SearchViewModel(
                this.createSavedStateHandle(),
                flightSearchApplication().container.flightsRepository
            )
        }
    }
}

fun CreationExtras.flightSearchApplication(): FlightSearchApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)