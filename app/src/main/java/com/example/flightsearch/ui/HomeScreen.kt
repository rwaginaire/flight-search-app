package com.example.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.FlightSearchTopAppBar
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun HomeScreen(
    onAirportClick: (Airport) -> Unit,
    airportSearchList: List<Airport>,
    searchText: String
) {
    if (searchText != "") {
        SearchResults(
            airportList = airportSearchList,
            onAirportClick = onAirportClick,
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