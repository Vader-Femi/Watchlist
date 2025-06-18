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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.company.watchlist.R
import com.company.watchlist.data.FavouritesTabCarousel
import com.company.watchlist.data.Film
import com.company.watchlist.data.ListType
import com.company.watchlist.presentation.favourites.FavouritesEvent
import com.company.watchlist.presentation.favourites.FavouritesState
import com.company.watchlist.ui.components.ErrorAlertDialog
import com.company.watchlist.ui.components.MyPullRefreshIndicator
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds


@OptIn(ExperimentalPagerApi::class)
@ExperimentalMaterialApi
@Composable
fun FavouritesScreen(
    state: FavouritesState,
    onEvent: (FavouritesEvent) -> Unit,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToSeriesDetails: (id: Int) -> Unit,
) {

    val pagerState = rememberPageState()
    val coroutineScope = rememberCoroutineScope()


    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onEvent(FavouritesEvent.GetList) }
    )


    if (state.error != null) {
        ErrorAlertDialog(state.error, { onEvent(FavouritesEvent.DismissError) }) {
            onEvent(FavouritesEvent.GetList)
        }
    }

    Column(
        modifier = Modifier
        .padding(15.dp, 0.dp, 15.dp, 0.dp)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            FavouritesTabCarousel.getItems().forEachIndexed { index, searchTabItem ->
                TabHeader(
                    title = searchTabItem.name,
                    isSelected = pagerState.currentPage == index,

                    ) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            }

        }

        HorizontalPager(
            count = FavouritesTabCarousel.getItems().size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState),
                verticalArrangement = Arrangement.Top
            ) {

                if (page == FavouritesTabCarousel.getItems()[0].index) {

                    if (state.favouritesMoviesList.isEmpty()) {
                        item(
                            key = "No favourite movie illustration"
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
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
                        key = { film -> film.id }
                    ) { film ->
                        FilmItem(
                            film = film,
                            navigateToMovieDetails = navigateToMovieDetails,
                            onRemove = { onEvent(FavouritesEvent.RemoveFromFavourites(film)) }
                        )
                    }
                }

                if (page == FavouritesTabCarousel.getItems()[1].index) {

                    if (state.favouritesSeriesList.isEmpty()) {
                        item(
                            key = "No favourite series illustration"
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
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
                        key = { film -> film.id }
                    ) { film ->
                        FilmItem(
                            film = film,
                            navigateToSeriesDetails = navigateToSeriesDetails,
                            onRemove = { onEvent(FavouritesEvent.RemoveFromFavourites(film)) }
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

@Composable
fun TabHeader(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp, bottom = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Tab(
            modifier = if (isSelected) Modifier
                .clip(AbsoluteRoundedCornerShape(30))
                .background(
                    MaterialTheme.colorScheme.primary
                )
//                .width(120.dp)
            else Modifier
                .clip(AbsoluteRoundedCornerShape(30))
                .background(
                    MaterialTheme.colorScheme.surface
                )
//                .width(120.dp)
            ,
            selected = isSelected,
            onClick = onClick,
            text = {
                Text(
                    text = title,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimary else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun rememberPageState(
    @androidx.annotation.IntRange(from = 0) initialPage: Int = 0,
): PagerState = rememberSaveable(saver = PagerState.Saver) {
    PagerState(
        currentPage = initialPage,
    )
}

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
    var showRemoveDialog by remember { mutableStateOf(false) }


    AnimatedVisibility(
        visible = show,
        exit = fadeOut(spring())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
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
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                ){
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
                                    locale = Locale.getDefault(),
                                    format = "%.2f",
                                    film.averageRating
                                )
                            }",
                            textAlign = TextAlign.Center
                        )
                    }

                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        onClick = { showRemoveDialog = true }
                    ) {
                        Icon(imageVector = Icons.TwoTone.Delete,
                            contentDescription = "Remove Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp))
                    }
                }
            }
        }
    }

    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Remove from Favourites") },
            text = { Text("Are you sure you want to remove \"${film.name}\" from your favourites?") },
            confirmButton = {
                TextButton(onClick = {
                    showRemoveDialog = false
                    show = false // triggers the animation and delayed removal
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Cancel")
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

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun FavouritesScreenPreview() {
    FavouritesScreen(
        state = FavouritesState(
            favouritesMoviesList = listOf(
                element = Film(
                    1L,
                    name = "Movie",
                    listType = ListType.FAVOURITESMOVIES,
                    averageRating = 34.34,
                    posterPath = "s"
                )
            ),
            favouritesSeriesList = listOf(
                element = Film(
                    2L,
                    name = "Series",
                    listType = ListType.FAVOURITESSERIES,
                    averageRating = 34.34,
                    posterPath = "s"
                )
            )
        ),
        onEvent = { },
        navigateToMovieDetails = { },
        navigateToSeriesDetails = {},
    )
}