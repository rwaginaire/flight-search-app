package com.example.flightsearch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport

@Composable
fun SearchResultsScreen(
    departureAirport: Airport,
    destinationAirportList: List<Airport>,
    onStarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "${stringResource(R.string.flights_from)} ${departureAirport.iataCode}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )

        LazyColumn(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            items(items = destinationAirportList) { destinationAirport ->
                FlightResult(
                    departureAirport = departureAirport,
                    destinationAirport = destinationAirport,
                    onStarClick = onStarClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun FlightResult(
    departureAirport: Airport,
    destinationAirport: Airport,
    onStarClick: () -> Unit,
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
                    airport = departureAirport,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)))

                Text(
                    text = stringResource(R.string.arrive),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_extra_small))
                )

                AirportSearchResult(airport = destinationAirport)
            }
            IconButton(
                enabled = true,
                onClick = { onStarClick() },

            ) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = stringResource(R.string.favorite)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlightResultPreview() {
    FlightResult(
        departureAirport = Airport(
            id = 0,
            iataCode = "LYS",
            name = "Lyon Saint-Exupéry Airport",
            passengers = 100
        ),
        destinationAirport = Airport(
            id = 1,
            iataCode = "MUC",
            name = "Munich International Airport",
            passengers = 200
        ),
        {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchResultsScreenPreview() {
    SearchResultsScreen(
        departureAirport = Airport(
            id = 0,
            iataCode = "LYS",
            name = "Lyon Saint-Exupéry Airport",
            passengers = 100
        ),
        destinationAirportList = listOf(
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
            ),
            Airport(
                id = 3,
                iataCode = "FCO",
                name = "Leonardo Da Vinci International Airport",
                passengers = 150
            ),
        ),
        {}
    )
}