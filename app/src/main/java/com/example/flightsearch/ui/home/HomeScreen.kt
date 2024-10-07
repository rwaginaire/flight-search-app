package com.example.flightsearch.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.flightsearch.NavigationDestination
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Flight
import com.example.flightsearch.ui.AirportSearchResult
import com.example.flightsearch.ui.search.FlightsDisplay

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    onAirportClick: (Airport) -> Unit,
    airportSearchList: List<Airport>,
    searchText: String,
    favoriteFlights: List<Flight>,
    onFavoriteClick: (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    if (searchText != "") {
        SearchResults(
            airportList = airportSearchList,
            onAirportClick = onAirportClick,
            modifier = modifier
        )
    }
    else {
        val title = if (favoriteFlights.isNotEmpty()) stringResource(R.string.favorite_routes) else stringResource(
            R.string.no_favorite
        )
        FlightsDisplay(
            title = title,
            flights = favoriteFlights,
            onFavoriteClick = onFavoriteClick,
            favoriteFlights = favoriteFlights
        )
    }
}

@Composable
private fun SearchResults(
    airportList: List<Airport>,
    onAirportClick: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = airportList, key = { it.id }) { item ->
            AirportSearchResult(airport = item,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth()
                    .clickable { onAirportClick(item) })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultsPreview() {
    SearchResults(
        listOf(
            Airport(
                id = 0,
                iataCode = "LYS",
                name = "Lyon Saint-Exupéry Airport",
                passengers = 100
            ),
            Airport(
                id = 1,
                iataCode = "MUC",
                name = "Munich International Airport",
                passengers = 200
            ),
            Airport(
                id = 2,
                iataCode = "ATH",
                name = "Athens International Airport",
                passengers = 150
            )
        ),
        {}
    )
}