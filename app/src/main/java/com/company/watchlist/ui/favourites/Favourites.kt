package com.company.watchlist.ui.favourites

import android.icu.util.LocaleData
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.company.watchlist.R
import com.company.watchlist.data.Film
import com.company.watchlist.data.ListType
import com.company.watchlist.data.FavouritesTabCarousel
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

    Column(modifier = Modifier) {
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
                    .pullRefresh(pullRefreshState)
                    .padding(15.dp, 0.dp, 15.dp, 0.dp),
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

    AnimatedVisibility(
        visible = show,
        exit = fadeOut(spring())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        OutlinedButton (
                            onClick = {
                                show = false
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier,
                            colors =  ButtonDefaults.outlinedButtonColors()
                        ) {
                            Text(
                                text = "Remove",
                            )
                        }

                        Button(
                            onClick = {
                                if (film.listType == ListType.FAVOURITESMOVIES)
                                    navigateToMovieDetails?.invoke(film.id.toInt())
                                else
                                    navigateToSeriesDetails?.invoke(film.id.toInt())
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier,
                        ) {
                            Text(
                                text = "View",
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(800.milliseconds)
            onRemove(currentItem)
            Toast.makeText(context, "${film.name} removed", Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color = when (dismissState.dismissDirection) {
//        DismissDirection.StartToEnd -> MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
        DismissDirection.EndToStart -> MaterialTheme.colorScheme.errorContainer
//        null -> Color.Transparent
        else -> Color.Unspecified
    }
    val direction = dismissState.dismissDirection


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
    ) {
//        if (direction == DismissDirection.StartToEnd) {
//            Text(
//                text = "View Details",
//                modifier = Modifier.align(Alignment.CenterStart),
//            )
//        }
        if (direction == DismissDirection.EndToStart) {
            Icon(
                Icons.TwoTone.Delete,
                contentDescription = "Delete",
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun OldFavouritesScreen(
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

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val positionalThreshold by remember { mutableStateOf((screenWidth * 0.8).dp) }

    LazyColumn(
        modifier = Modifier
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item(
            key = "Movies Text"
        ) {
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
            OldFilmItem(
                film = film,
                positionalThreshold = positionalThreshold,
                navigateToMovieDetails = navigateToMovieDetails,
                onRemove = { onEvent(FavouritesEvent.RemoveFromFavourites(film)) }
            )
        }


        item(
            key = "Series Text"
        ) {
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
            OldFilmItem(
                film = film,
                positionalThreshold = positionalThreshold,
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
fun OldFilmItem(
    film: Film,
    positionalThreshold: Dp,
    navigateToMovieDetails: ((id: Int) -> Unit)? = null,
    navigateToSeriesDetails: ((id: Int) -> Unit)? = null,
    onRemove: (Film) -> Unit,
) {

    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(film)
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                show = false
                true
            } else {
                if (it == DismissValue.DismissedToEnd) {
                    if (film.listType == ListType.FAVOURITESMOVIES)
                        navigateToMovieDetails?.invoke(film.id.toInt())
                    else
                        navigateToSeriesDetails?.invoke(film.id.toInt())
                }
                false
            }
        }, positionalThreshold = { positionalThreshold.toPx() }
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
            directions = setOf(DismissDirection.EndToStart),
            dismissContent = {

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