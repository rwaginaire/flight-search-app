package com.example.flightsearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.FlightSearchViewModel
import com.example.flightsearch.ui.HomeScreen
import com.example.flightsearch.ui.SearchBar
import com.example.flightsearch.ui.SearchResultsScreen
import com.example.flightsearch.ui.toAirport

enum class FlightSearchScreens {
    Home,
    SearchResults
}

@Composable
fun FlightSearchApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: FlightSearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            FlightSearchTopAppBar(
                title = stringResource(R.string.flight_search),
                canNavigateBack = navController.previousBackStackEntry != null,
                onBackClick = { navController.navigateUp() },
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                inputText = uiState.searchText,
                onTextChange = { viewModel.updateSearchText(it) },
                modifier = Modifier.padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
            )
            NavHost(
                navController = navController,
                startDestination = FlightSearchScreens.Home.name,
                modifier = modifier
            ) {
                composable(route = FlightSearchScreens.Home.name) {
                    val searchResultList by viewModel.searchAirportsWithText(uiState.searchText).collectAsState(emptyList())
                    HomeScreen(
                            onAirportClick = { airport ->
                                viewModel.updateSelectedAirport(airport)
                                navController.navigate(
                                    "${FlightSearchScreens.SearchResults.name}/${airport.id}"
                                )
                            },
                        searchResultList,
                        uiState.searchText
                    )
                }
                val airportIdArgument = "airportId"
                composable(
                    route = FlightSearchScreens.SearchResults.name + "/{$airportIdArgument}",
                    arguments = listOf(navArgument(airportIdArgument) { type = NavType.IntType })
                ) { backStackEntry ->
                    val airportId = backStackEntry.arguments?.getInt(airportIdArgument)
                        ?: error("airportIdArgument cannot be null")
                    val destinationAirportList by viewModel.getPossibleDestinationsFromAirport(airportId).collectAsState(emptyList())
                    SearchResultsScreen(
                        departureAirport = uiState.selectedAirportDetails.toAirport(),
                        destinationAirportList = destinationAirportList,
                        onStarClick = {}
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (canNavigateBack) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            },
            modifier = modifier
        )
    } else {
        TopAppBar(
            title = { Text(title) },
            modifier = modifier
        )
    }
}