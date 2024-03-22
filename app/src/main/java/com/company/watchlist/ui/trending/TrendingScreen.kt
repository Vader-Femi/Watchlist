package com.company.watchlist.ui.trending

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.company.watchlist.data.remote.response.trending.TrendingResult
import com.company.watchlist.presentation.trending.TrendingEvent
import com.company.watchlist.presentation.trending.TrendingState
import com.company.watchlist.ui.components.ErrorAlertDialog
import com.company.watchlist.ui.components.MyPullRefreshIndicator

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrendingScreen(
    state: TrendingState,
    trendingEvent: (TrendingEvent) -> Unit,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToSeriesDetails: (id: Int) -> Unit
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { trendingEvent(TrendingEvent.GetTrending) }
    )

    if (state.error != null) {
        ErrorAlertDialog(state.error, {trendingEvent(TrendingEvent.DismissError)}) {
            trendingEvent(TrendingEvent.GetTrending)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(10.dp, 8.dp, 10.dp, 0.dp)
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        state.trendingList.forEach { trendingItem ->
            item (
                key = trendingItem.id
            ){
                Card(
                    modifier = Modifier
                        .padding(10.dp, 8.dp, 10.dp, 8.dp)
                        .fillMaxWidth()
                        .clickable {
                            if (trendingItem.media_type == "movie") navigateToMovieDetails(
                                trendingItem.id
                            )
                            else if (trendingItem.media_type == "tv") navigateToSeriesDetails(
                                trendingItem.id
                            )
                        },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {

                            Row {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxSize(0.4f)
                                        .padding(4.dp),
                                    model = "https://image.tmdb.org/t/p/w500/${trendingItem.poster_path}",
                                    contentDescription = "${trendingItem.title ?: trendingItem.name} Poster"
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "${trendingItem.title ?: trendingItem.name}",
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = trendingItem.media_type.replaceFirstChar { it.uppercaseChar() },
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.outline
                                    )

                                    if (trendingItem.first_air_date != null) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 5.dp, bottom = 5.dp),
                                            text = "First aired ${trendingItem.first_air_date}",
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    if (trendingItem.release_date != null) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 5.dp, bottom = 5.dp),
                                            text = "Released: ${trendingItem.release_date}",
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "Original language: ${trendingItem.original_language.uppercase()}",
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp),
                                        text = "Average Rating: ${String.format("%.2f", trendingItem.vote_average)}",
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                        }

                        Text(
                            text = trendingItem.overview,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }

    MyPullRefreshIndicator(
        isLoading = state.isLoading,
        pullRefreshState = pullRefreshState
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TrendingScreenPreview() {
    TrendingScreen(
        state = TrendingState(
            error = null,
            trendingList = listOf(
                TrendingResult(
                    true,
                    "",
                    "",
                    emptyList(),
                    1,
                    "Movie",
                    "MovieName",
                    "English",
                    "",
                    "",
                    "Lorem ipson",
                    0.0,
                    "",
                    "2023-12-15",
                    "Series Title",
                    false,
                    5.6,
                    0
                ),
                TrendingResult(
                    true,
                    "",
                    "",
                    emptyList(),
                    1,
                    "Tv",
                    "MovieName",
                    "English",
                    "",
                    "",
                    "Lorem ipson",
                    0.0,
                    "",
                    "2023-12-15",
                    "Series Title",
                    false,
                    5.6,
                    0
                )
            )
        ),
        trendingEvent = {},
        navigateToMovieDetails = {},
        navigateToSeriesDetails = {}
    )
}