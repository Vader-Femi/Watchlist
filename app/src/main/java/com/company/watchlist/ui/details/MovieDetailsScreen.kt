package com.company.watchlist.ui.details

import android.content.Intent
import android.icu.text.NumberFormat
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
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
import com.company.watchlist.data.remote.response.details.movie.Genre
import com.company.watchlist.data.remote.response.details.movie.ProductionCompany
import com.company.watchlist.data.remote.response.details.movie.SpokenLanguage
import com.company.watchlist.presentation.details.movie.MovieDetailsEvent
import com.company.watchlist.presentation.details.movie.MovieDetailsState
import com.company.watchlist.ui.components.ErrorAlertDialog
import java.util.Locale


@Composable
fun MovieDetailsScreen(
    movieId: Int,
    state: MovieDetailsState,
    movieDetailsEvent: (MovieDetailsEvent) -> Unit,
) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        movieDetailsEvent(MovieDetailsEvent.SetId(movieId))
        movieDetailsEvent(MovieDetailsEvent.GetDetails)
    }

    if (state.error != null) {
        ErrorAlertDialog(state.error) {
            movieDetailsEvent(MovieDetailsEvent.GetDetails)
        }
    }

    Column(
        modifier = Modifier
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {

            Row {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(0.4f)
                        .padding(8.dp),
                    model = "https://image.tmdb.org/t/p/w500/${state.posterPath}",
                    contentDescription = "${state.title} Poster"
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
                        text = state.title ?: "",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        text = "Released: ${state.release_date}",
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        text = "Original language: ${state.original_language?.uppercase()}",
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        text = "Average Rating: ${state.voteAverage}",
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

        Divider(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
        )

        Text(
            text = state.overview ?: "",
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Left
        )

        Divider(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
        )

        if (state.original_title != state.title) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Original Title: ${state.original_title}",
                textAlign = TextAlign.Left
            )
        }

        if (state.tagline?.isNotBlank() == true) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = state.tagline,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        if (state.runtime != 0) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Runtime: ${state.runtime} Minutes",
                textAlign = TextAlign.Left
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp),
            text = "Status: " + state.status?.ifBlank { "Unknown" },
            textAlign = TextAlign.Left
        )

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

        if (state.budget != 0) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Budget: ${convertToCurrency(state.budget)}",
                textAlign = TextAlign.Left
            )
        }

        if (state.revenue != 0) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Revenue: ${convertToCurrency(state.revenue)}",
                textAlign = TextAlign.Left
            )
        }

        if (state.revenue != 0 && state.budget != 0) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Money Made/Lost: ${convertToCurrency(state.revenue?.minus(state.budget ?: 0))}",
                textAlign = TextAlign.Left
            )
        }

        if (state.spoken_languages.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    Text(
                        text = "Spoken Language(s)"
                    )
                }

                state.spoken_languages.forEach { language ->
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
                                text = language.name.replaceFirstChar { it.uppercaseChar() },
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

            }
        }

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

        if (state.production_companies.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    Text(
                        text = "Production Compan(ies)"
                    )
                }

                state.production_companies.forEach { productionCompany ->
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
                                text = productionCompany.name,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

            }
        }
    }
}

private fun convertToCurrency(number: Int?): String {
    return NumberFormat.getCurrencyInstance(Locale("en", "US")).format(number ?: 0)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MovieDetailsScreenPreview() {
    MovieDetailsScreen(
        0,
        MovieDetailsState(
            budget = -1,
            genres = listOf(
                Genre(
                    id = 0,
                    name = "Action"
                )
            ),
            homepage = "",
            id = -1,
            original_language = "",
            original_title = "Old Title",
            overview = "The three-way struggle between the Navy, the Blackbeard Pirates, and the Kuja Pirates comes to an end when an unexpected figure shows up. On the Sunny, the Straw Hats talk about the recent incidents around the world and Luffy’s words surprise everyone.",
            title = "New Title",
            posterPath = "/gr",
            voteAverage = 5.4,
            production_companies = listOf(
                ProductionCompany(
                    3,
                    "/fsdf",
                    "Company",
                    "Nigeria"
                )
            ),
            release_date = "2020-12-09",
            revenue = -1,
            runtime = -1,
            spoken_languages = listOf(
                SpokenLanguage(
                    english_name = "Japanese",
                    iso_639_1 = "J",
                    name = "J"
                )
            ),
            status = "Released",
            tagline = "It's all about family",
            isLoading = false,
            error = null
        )
    ) {

    }
}
