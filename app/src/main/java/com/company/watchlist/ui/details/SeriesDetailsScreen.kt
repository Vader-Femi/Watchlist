package com.company.watchlist.ui.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.company.watchlist.data.remote.response.details.series.Genre
import com.company.watchlist.data.remote.response.details.series.Season
import com.company.watchlist.data.remote.response.details.series.SpokenLanguage
import com.company.watchlist.presentation.details.series.SeriesDetailsEvent
import com.company.watchlist.presentation.details.series.SeriesDetailsState
import com.company.watchlist.ui.components.ErrorAlertDialog
import com.company.watchlist.ui.components.MyPullRefreshIndicator

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SeriesDetailsScreen(
    seriesId: Int,
    state: SeriesDetailsState,
    seriesDetailsEvent: (SeriesDetailsEvent) -> Unit
) {

    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { seriesDetailsEvent(SeriesDetailsEvent.GetDetails) }
    )

    LaunchedEffect(key1 = true) {
        seriesDetailsEvent(SeriesDetailsEvent.SetId(seriesId.toLong()))
        seriesDetailsEvent(SeriesDetailsEvent.GetDetails)
    }


    if (state.error != null) {
        ErrorAlertDialog(state.error, {seriesDetailsEvent(SeriesDetailsEvent.DismissError)}) {
            seriesDetailsEvent(SeriesDetailsEvent.GetDetails)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(10.dp, 8.dp, 10.dp, 0.dp)
            .fillMaxWidth()
            .pullRefresh(pullRefreshState),
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.Center
            ) {

                Row {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(0.4f)
                            .padding(8.dp),
                        model = "https://image.tmdb.org/t/p/w500/${state.posterPath}",
                        contentDescription = "${state.name} Poster"
                    )
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterVertically),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp, bottom = 5.dp),
                            text = state.name ?: "",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "First aired ${state.firstAirDate}",
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "Last aired ${state.lastAirDate}",
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "Original language: ${state.originalLanguage?.uppercase()}",
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "Average Rating: ${String.format("%.2f", state.voteAverage)}",
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }

        item {
            if (state.overview?.isNotEmpty() == true)
                Text(
                    text = state.overview,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Left
                )
        }

        item {
            Divider(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
            )
        }

        item {
            if (state.originalName != state.name) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    text = "Original Title: ${state.originalName}",
                    textAlign = TextAlign.Left
                )
            }
        }

        item {
            if (state.tagline?.isNotBlank() == true) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    text = "Tagline: ${state.tagline}",
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        item {
            if (state.numberOfSeasons != 0) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    text = "Number of Seasons: ${state.numberOfSeasons}",
                    textAlign = TextAlign.Left
                )
            }
        }

        item {
            if (state.numberOfEpisodes != 0) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    text = "Number of Episodes: ${state.numberOfEpisodes}",
                    textAlign = TextAlign.Left
                )
            }
        }

        item {
            if (state.lastEpisodeToAir != null) {
                Divider(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    text = "Last Episode to Air",
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.tertiary
                )
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
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {

                            Row {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxSize(0.4f)
                                        .align(Alignment.CenterVertically)
                                        .padding(4.dp),
                                    model = "https://image.tmdb.org/t/p/w500/${state.lastEpisodeToAir.still_path}",
                                    contentDescription = "Last Episode to Air Poster"
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
                                        text = state.lastEpisodeToAir.name,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "Air Date: ${state.lastEpisodeToAir.air_date}",
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "Season ${state.lastEpisodeToAir.season_number} Episode ${state.lastEpisodeToAir.episode_number}",
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "${state.lastEpisodeToAir.episode_type.replaceFirstChar { it.uppercaseChar() }} Episode",
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "Average Rating: ${state.lastEpisodeToAir.vote_average}",
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "Runtime: ${state.lastEpisodeToAir.runtime} Minutes",
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                        }

                        Text(
                            text = state.lastEpisodeToAir.overview,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                }
                Divider(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                )
            }
        }

        item {
            if (state.nextEpisodeToAir != null) {
                Divider(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    text = "Next Episode to Air",
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.tertiary
                )
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
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {

                            Row {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxSize(0.4f)
                                        .align(Alignment.CenterVertically)
                                        .padding(4.dp),
                                    model = "https://image.tmdb.org/t/p/w500/${state.nextEpisodeToAir.still_path}",
                                    contentDescription = "Next Episode to Air Poster"
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
                                        text = state.nextEpisodeToAir.name,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "Air Date: ${state.nextEpisodeToAir.air_date}",
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "Season ${state.nextEpisodeToAir.season_number} Episode ${state.nextEpisodeToAir.episode_number}",
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp),
                                        text = "${state.nextEpisodeToAir.episode_type} Episode",
                                        textAlign = TextAlign.Center
                                    )

                                    if (state.nextEpisodeToAir.vote_average != 0) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 5.dp, bottom = 5.dp),
                                            text = "Average Rating: ${state.nextEpisodeToAir.vote_average}",
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    if (state.nextEpisodeToAir.runtime != 0) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 5.dp, bottom = 5.dp),
                                            text = "Runtime: ${state.nextEpisodeToAir.runtime}",
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                        }

                        Text(
                            text = state.nextEpisodeToAir.overview,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                }
                Divider(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                )
            }
        }

        item {
            if (state.homepage?.isNotBlank() == true) {

                val annotatedText = buildAnnotatedString {
                    pushStringAnnotation(
                        tag = "Homepage",
                        annotation = state.homepage
                    )

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                        ),
                    ) {
                        append("Homepage: ")
                    }

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                        ),
                    ) {
                        append(state.homepage)
                    }

                    pop()
                }
                ClickableText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    text = annotatedText,
                    style = TextStyle.Default.copy(
                        textAlign = TextAlign.Left
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(
                            tag = "Homepage",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let {
                                val homepageIntent = Intent(Intent.ACTION_VIEW)
                                homepageIntent.setData(Uri.parse(state.homepage))
                                context.startActivity(homepageIntent)
                            }
                    }
                )
            }
        }

        item {
            if (state.genres.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        Text(
                            text = "Genre(s)"
                        )
                    }

                    state.genres.forEach { genres ->
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(7.dp, 3.dp, 7.dp, 3.dp),
                                    text = genres.name,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                }
            }
        }

        item {
            if (state.languages.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        Text(
                            text = "Language(s)"
                        )
                    }

                    state.languages.forEach { language ->
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(7.dp, 3.dp, 7.dp, 3.dp),
                                    text = language.replaceFirstChar { it.uppercaseChar() },
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                }
            }
        }

        item {
            if (state.seasons.isNotEmpty()) {
                Divider(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    text = "All Seasons",
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.tertiary
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    state.seasons.forEach { season ->
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
                                    Column(
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Row {
                                            AsyncImage(
                                                modifier = Modifier
                                                    .fillMaxSize(0.4f)
                                                    .padding(8.dp),
                                                model = "https://image.tmdb.org/t/p/w500/${season.poster_path}",
                                                contentDescription = "Season ${season.season_number} Poster"
                                            )
                                            Column(
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .align(Alignment.CenterVertically),
                                                verticalArrangement = Arrangement.Center
                                            ) {

                                                Text(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 5.dp, bottom = 5.dp),
                                                    text = season.name,
                                                    textAlign = TextAlign.Center
                                                )

                                                Text(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 5.dp, bottom = 5.dp),
                                                    text = "Season Number: ${season.season_number}",
                                                    textAlign = TextAlign.Center
                                                )

                                                Text(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 5.dp, bottom = 5.dp),
                                                    text = "Number of Episodes: ${season.episode_count}",
                                                    textAlign = TextAlign.Center
                                                )

                                                Text(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 5.dp, bottom = 5.dp),
                                                    text = "Aired Date ${season.air_date}",
                                                    textAlign = TextAlign.Center
                                                )

                                                Text(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 5.dp, bottom = 5.dp),
                                                    text = "Average Rating: ${season.vote_average}",
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }

                                    }
                                }

                            }
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(75.dp))
        }
    }


    MyPullRefreshIndicator(
        isLoading = state.isLoading,
        pullRefreshState = pullRefreshState
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeriesDetailsScreenPreview() {
    SeriesDetailsScreen(
        0,
        SeriesDetailsState(
            id = -1,
            firstAirDate = "2020-12-09",
            homepage = "",
            name = "New Name",
            originalName = "Old Name",
            originalLanguage = "Eng",
            spokenLanguage = listOf(
                SpokenLanguage(
                    english_name = "Japanese",
                    iso_639_1 = "J",
                    name = "J"
                )
            ),
            overview = "The three-way struggle between the Navy, the Blackbeard Pirates, and the Kuja Pirates comes to an end when an unexpected figure shows up. On the Sunny, the Straw Hats talk about the recent incidents around the world and Luffy’s words surprise everyone.",
            tagline = "Set sail for one piece",
            voteAverage = 5.4,
            numberOfSeasons = 5,
            numberOfEpisodes = 6,
//            lastEpisodeToAir = LastEpisodeToAir(
//                air_date = "2020-12-09",
//                episode_number = 5,
//                episode_type = "standard",
//                id = 5044229,
//                name = "Luffy's Dream",
//                overview = "The three-way struggle between the Navy, the Blackbeard Pirates, and the Kuja Pirates comes to an end when an unexpected figure shows up. On the Sunny, the Straw Hats talk about the recent incidents around the world and Luffy’s words surprise everyone.",
//                production_code = "",
//                runtime = 24,
//                season_number = 21,
//                show_id = 37854,
//                still_path = "/8H54bCkcqBMCripAyYvE7eQT3Ef.jpg",
//                vote_average = 5.4,
//                vote_count = 0,
//            ),
//            nextEpisodeToAir = NextEpisodeToAir(
//                air_date = "2020-12-09",
//                episode_number = 5,
//                episode_type = "standard",
//                id = 5044229,
//                name = "Luffy's Dream",
//                overview = "The three-way struggle between the Navy, the Blackbeard Pirates, and the Kuja Pirates comes to an end when an unexpected figure shows up. On the Sunny, the Straw Hats talk about the recent incidents around the world and Luffy’s words surprise everyone.",
//                production_code = "",
//                runtime = 24,
//                season_number = 21,
//                show_id = 37854,
//                still_path = "/8H54bCkcqBMCripAyYvE7eQT3Ef.jpg",
//                vote_average = 5,
//                vote_count = 0,
//            ),
            lastEpisodeToAir = null,
            seasons = listOf(
                Season(
                    air_date = "1999-12-22",
                    episode_count = 27,
                    id = 49191,
                    name = "Specials",
                    overview = "",
                    poster_path = "/d3TJkAgh8JZXrlD9z8R0pUgwIsx.jpg",
                    season_number = 0,
                    vote_average = 0.0
                )
            ),
            genres = listOf(
                Genre(
                    id = 0,
                    name = "Action"
                )
            ),
            languages = listOf(
                "Eng",
                "Jap"
            ),
            isLoading = false,
            error = null
        )
    ) {

    }
}