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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.SearchBar
import com.example.flightsearch.ui.home.HomeDestination
import com.example.flightsearch.ui.home.HomeScreen
import com.example.flightsearch.ui.home.HomeViewModel
import com.example.flightsearch.ui.search.SearchDestination
import com.example.flightsearch.ui.search.SearchResultsScreen
import com.example.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun FlightSearchApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val favoritesUiState by viewModel.favoritesUiState.collectAsState()
    Scaffold(
        topBar = {
            FlightSearchTopAppBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = navController.previousBackStackEntry != null,
                onBackClick = { navController.navigateUp() },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(dimensionResource(R.dimen.padding_small))
        ) {
            val focusManager = LocalFocusManager.current
            SearchBar(
                inputText = homeUiState.searchText,
                onTextChange = {
                    viewModel.updateSearchText(it)
                },
                onClick = { navController.navigateUp() },
                onClearClick = {
                    viewModel.updateSearchText("")
                    navController.navigateUp()
                },
                modifier = Modifier.padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
            )
            NavHost(
                navController = navController,
                startDestination = HomeDestination.route
            ) {
                composable(route = HomeDestination.route) {
                    HomeScreen(
                        onAirportClick = { airport ->
                            navController.navigate(
                                "${SearchDestination.route}/${airport.id}"
                            )
                            focusManager.clearFocus()
                        },
                        searchText = homeUiState.searchText,
                        airportSearchList = homeUiState.searchedAirports,
                        favoriteFlights = favoritesUiState.favoriteFlights,
                        onFavoriteClick = viewModel::deleteFavoriteFlight
                    )
                }
                composable(
                    route = SearchDestination.routeWithArgs,
                    arguments = listOf(navArgument(SearchDestination.airportIdArg) {
                        type = NavType.IntType
                    })
                ) {
                    SearchResultsScreen(
                        favoriteFlights = favoritesUiState.favoriteFlights,
                        onRouteFavoriteClick = viewModel::addOrDeleteFavoriteRoute
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
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun FlightSearchTopAppBarPreview() {
    FlightSearchTheme {
        FlightSearchTopAppBar(
            title = stringResource(R.string.app_name),
            false,
            {}
        )
    }
}