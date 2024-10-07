package com.example.flightsearch.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.NavigationDestination
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.FavoriteFlight
import com.example.flightsearch.data.Flight
import com.example.flightsearch.data.MockData
import com.example.flightsearch.ui.AirportSearchResult
import com.example.flightsearch.ui.AppViewModelProvider

object SearchDestination : NavigationDestination {
    override val route = "search"
    override val titleRes = R.string.search_results
    const val airportIdArg = "airportId"
    val routeWithArgs = "$route/{$airportIdArg}"
}

@Composable
fun SearchResultsScreen(
    favoriteFlights: List<Flight>,
    onRouteFavoriteClick: (Flight) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    SearchResultsBody(
        departureAirport = uiState.departureAirport,
        possibleFlights = uiState.flightsList,
        onFavoriteClick = onRouteFavoriteClick,
        favoriteFlights = favoriteFlights,
        modifier = modifier
    )
}

@Composable
fun FlightsDisplay(
    title: String,
    flights: List<Flight>,
    onFavoriteClick: (Flight) -> Unit,
    favoriteFlights: List<Flight>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )

        LazyColumn(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            items(items = flights) { flight ->
                val isFavorite = favoriteFlights.any { f ->
                    f.departureAirport == flight.departureAirport &&
                            f.destinationAirport == flight.destinationAirport }
                FlightResult(
                    flight = flight,
                    onFavoriteClick = { onFavoriteClick(flight) },
                    isFavorite = isFavorite,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SearchResultsBody(
    departureAirport: Airport,
    possibleFlights: List<Flight>,
    onFavoriteClick: (Flight) -> Unit,
    favoriteFlights: List<Flight>,
    modifier: Modifier = Modifier,
) {
    val title = "${stringResource(R.string.flights_from)} ${departureAirport.iataCode}"

    FlightsDisplay(
        title = title,
        flights = possibleFlights,
        onFavoriteClick = onFavoriteClick,
        favoriteFlights = favoriteFlights,
        modifier = modifier
    )
}

@Composable
private fun FlightResult(
    flight: Flight,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.depart),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_extra_small))
                )

                AirportSearchResult(
                    airport = flight.departureAirport,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)))

                Text(
                    text = stringResource(R.string.arrive),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_extra_small))
                )

                AirportSearchResult(airport = flight.destinationAirport)
            }
            IconButton(
                enabled = true,
                onClick = { onFavoriteClick() },

            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(R.string.favorite),
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else
                        Color.Unspecified
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlightResultPreview() {
    FlightResult(
        flight = MockData.flights[0],
        {},
        false
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchResultsBodyPreview() {
    SearchResultsBody(
        departureAirport = MockData.airports[0],
        possibleFlights = MockData.flights,
        onFavoriteClick = {},
        favoriteFlights = emptyList()
    )
}