package com.company.watchlist.ui.favourites

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
        ErrorAlertDialog(state.error, {onEvent(FavouritesEvent.DismissError)}) {
            onEvent(FavouritesEvent.GetList)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
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

        if (state.favouritesMoviesList.isEmpty()){
            item {
                Image(
                    painter = painterResource(id = R.drawable.empty_amico),
                    contentScale = ContentScale.Fit,
                    contentDescription = "No favourite movie illustration"
                )
            }
        }

        for (film in state.favouritesMoviesList) {
            item {
                FilmItem(
                    film = film,
                    navigateToMovieDetails = navigateToMovieDetails
                )
            }
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


        if (state.favouritesSeriesList.isEmpty()){
            item {
                Image(
                    painter = painterResource(id = R.drawable.emptybro),
                    contentScale = ContentScale.Fit,
                    contentDescription = "No favourite series illustration"
                )
            }
        }


        for (film in state.favouritesSeriesList) {
            item {
                FilmItem(
                    film = film,
                    navigateToSeriesDetails = navigateToSeriesDetails
                )
            }
        }
    }

    MyPullRefreshIndicator(
        isLoading = state.isLoading,
        pullRefreshState = pullRefreshState
    )

}

@Composable
fun FilmItem(
    film: Film,
    navigateToMovieDetails: ((id: Int) -> Unit)? = null,
    navigateToSeriesDetails: ((id: Int) -> Unit)? = null,
) {
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

            if (film.posterPath.isNullOrBlank()){
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
                    text = "Average Rating: ${String.format("%.2f", film.averageRating)}",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}