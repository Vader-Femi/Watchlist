package com.company.watchlist.ui.favourites

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.company.watchlist.R
import com.company.watchlist.data.Film
import com.company.watchlist.data.ListType
import com.company.watchlist.presentation.favourites.FavouritesEvent
import com.company.watchlist.presentation.favourites.FavouritesState
import com.company.watchlist.ui.components.ErrorAlertDialog
import com.company.watchlist.ui.components.MyPullRefreshIndicator
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@ExperimentalMaterialApi
@Composable
fun FavouritesScreen(
    state: FavouritesState,
    onEvent: (FavouritesEvent) -> Unit,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToSeriesDetails: (id: Int) -> Unit,
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onEvent(FavouritesEvent.GetList) }
    )

    if (state.error != null) {
        ErrorAlertDialog(state.error, { onEvent(FavouritesEvent.DismissError) }) {
            onEvent(FavouritesEvent.GetList)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Text(
                text = "Movies",
                modifier = Modifier
                    .padding(10.dp, 8.dp, 10.dp, 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.primary
            )
        }


        if (state.favouritesMoviesList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empty_amico),
                        contentScale = ContentScale.Fit,
                        contentDescription = "No favourite movie illustration"
                    )
                }
            }
        }

        items(
            items = state.favouritesMoviesList,
            key = { film -> film.hashCode() }
        ) { film ->
            FilmItem(
                film = film,
                navigateToMovieDetails = navigateToMovieDetails,
                onRemove = { onEvent(FavouritesEvent.RemoveFromFavourites(film)) }
            )
        }


        item {
            Text(
                text = "Series",
                modifier = Modifier
                    .padding(10.dp, 8.dp, 10.dp, 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.primary
            )
        }


        if (state.favouritesSeriesList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.emptybro),
                        contentScale = ContentScale.Fit,
                        contentDescription = "No favourite series illustration"
                    )
                }
            }
        }

        items(
            items = state.favouritesSeriesList,
            key = { film -> film.hashCode() }
        ) { film ->
            FilmItem(
                film = film,
                navigateToSeriesDetails = navigateToSeriesDetails,
                onRemove = { onEvent(FavouritesEvent.RemoveFromFavourites(film)) }
            )
        }


    }

    MyPullRefreshIndicator(
        isLoading = state.isLoading,
        pullRefreshState = pullRefreshState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> MaterialTheme.colorScheme.errorContainer
        DismissDirection.EndToStart -> MaterialTheme.colorScheme.errorContainer
        null -> Color.Transparent
    }
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == DismissDirection.StartToEnd) Icon(
            Icons.TwoTone.Delete,
            contentDescription = "Delete"
        )
        Spacer(modifier = Modifier)
        if (direction == DismissDirection.EndToStart) Icon(
            Icons.TwoTone.Delete,
            contentDescription = "Delete"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmItem(
    film: Film,
    navigateToMovieDetails: ((id: Int) -> Unit)? = null,
    navigateToSeriesDetails: ((id: Int) -> Unit)? = null,
    onRemove: (Film) -> Unit,
) {

    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(film)
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart || it == DismissValue.DismissedToEnd) {
                show = false
                true
            } else false
        }, positionalThreshold = { 150.dp.toPx() }
    )

    AnimatedVisibility(
        visible = show,
        exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {

                Card(
                    modifier = Modifier
                        .padding(10.dp, 8.dp, 10.dp, 8.dp)
                        .fillMaxWidth()
                        .clickable {
                            if (film.listType == ListType.FAVOURITESMOVIES)
                                navigateToMovieDetails?.invoke(film.id.toInt())
                            else
                                navigateToSeriesDetails?.invoke(film.id.toInt())
                        },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .height(200.dp)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        if (film.posterPath.isNullOrBlank()) {
                            Image(
                                painter = painterResource(id = R.drawable.film_rolls_amico),
                                modifier = Modifier
                                    .fillMaxWidth(0.4f),
                                contentScale = ContentScale.Fit,
                                contentDescription = "${film.name} Poster"
                            )

                        } else {
                            AsyncImage(
                                contentScale = ContentScale.FillHeight,
                                placeholder = painterResource(id = R.drawable.film_rolls_amico),
                                fallback = painterResource(id = R.drawable.film_rolls_amico),
                                model = "https://image.tmdb.org/t/p/w500/${film.posterPath}",
                                contentDescription = "${film.name} Poster"
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, bottom = 5.dp),
                                text = film.name,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.tertiary
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, bottom = 10.dp),
                                text = "Average Rating: ${
                                    String.format(
                                        "%.2f",
                                        film.averageRating
                                    )
                                }",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(800.milliseconds)
            onRemove(currentItem)
            Toast.makeText(context, "${film.name} removed", Toast.LENGTH_SHORT).show()
        }
    }
}