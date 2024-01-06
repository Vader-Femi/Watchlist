package com.company.watchlist.ui.trending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.company.watchlist.presentation.trending.TrendingState

@Composable
fun TrendingScreen(
    state: TrendingState,
    getTrending: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        getTrending.invoke()
    }

    LazyColumn {
        state.trendingList.forEach { trendingItem ->
            item {
                Card(
                    modifier = Modifier
                        .padding(10.dp, 8.dp, 10.dp, 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp),
                    ) {
                        Row {
                            AsyncImage(
                                modifier = Modifier.width(180.dp),
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
                                        .padding(top = 10.dp, bottom = 10.dp),
                                    text = trendingItem.media_type.replaceFirstChar { it.uppercaseChar() },
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.tertiary
                                )

                                if (trendingItem.first_air_date != null) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp, bottom = 10.dp),
                                        text = "First aired ${trendingItem.first_air_date}",
                                        textAlign = TextAlign.Center
                                    )
                                }
                                if (trendingItem.release_date != null) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp, bottom = 10.dp),
                                        text = "Released: ${trendingItem.release_date}",
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, bottom = 10.dp),
                                    text = "Original language: ${trendingItem.original_language.uppercase()}",
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, bottom = 10.dp),
                                    text = "Average Rating: ${trendingItem.vote_average}",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Text(
                            modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp),
                            text = "${trendingItem.title ?: trendingItem.name}",
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        Text(
                            text = trendingItem.overview,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }


}