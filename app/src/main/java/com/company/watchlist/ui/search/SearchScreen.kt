package com.company.watchlist.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.company.watchlist.data.SearchTabCarousel
import com.company.watchlist.data.remote.response.search.movie.SearchMovieResult
import com.company.watchlist.data.remote.response.search.series.SearchSeriesResult
import com.company.watchlist.presentation.search.movies.SearchMovieEvent
import com.company.watchlist.presentation.search.movies.SearchMovieState
import com.company.watchlist.presentation.search.series.SearchSeriesEvent
import com.company.watchlist.presentation.search.series.SearchSeriesState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SearchSeriesScreen(
    searchMovieState: SearchMovieState,
    searchSeriesState: SearchSeriesState,
    onMovieEvent: (SearchMovieEvent) -> Unit,
    onSeriesEvent: (SearchSeriesEvent) -> Unit,
    navigateToMovieDetails: (id :Int) -> Unit,
    navigateToSeriesDetails: (id :Int) -> Unit
) {
    val pagerState = rememberPageState()
    val coroutineScope = rememberCoroutineScope()
    val moviePageIndex by remember { mutableIntStateOf(0) }
    val seriesPageIndex by remember { mutableIntStateOf(1) }
    val movieList: LazyPagingItems<SearchMovieResult> =
        searchMovieState.searchResult.collectAsLazyPagingItems()
    val seriesList = searchSeriesState.searchResult.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.padding(15.dp, 0.dp, 15.dp, 0.dp)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
//            indicator = {}
        ) {
            SearchTabCarousel.getItems().forEachIndexed { index, searchTabItem ->
                TabHeader(
                    title = searchTabItem.name,
                    isSelected = pagerState.currentPage == index
                ) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            }

        }

        HorizontalPager(
            count = SearchTabCarousel.getItems().size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {

                item {
                    OutlinedTextField(
                        value = if (page == moviePageIndex)
                            searchMovieState.query else searchSeriesState.query,
                        onValueChange = {
                            if (page == moviePageIndex) onMovieEvent(
                                SearchMovieEvent.SearchQueryChanged(
                                    it
                                )
                            ) else onSeriesEvent(SearchSeriesEvent.SearchQueryChanged(it))
                        },
                        placeholder = { Text(text = if (page == moviePageIndex) "Search Movie" else "Search Series")},
                        isError = if (page == moviePageIndex) searchMovieState.searchError != null else searchSeriesState.searchError != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (page == moviePageIndex) onMovieEvent(SearchMovieEvent.Search) else onSeriesEvent(
                                    SearchSeriesEvent.Search
                                )
                            }
                        )
                    )
                }

                item {
                    if (if (page == moviePageIndex) searchMovieState.searchError != null else searchSeriesState.searchError != null) {
                        Text(
                            text = if (page == moviePageIndex) searchMovieState.searchError
                                ?: "" else searchSeriesState.searchError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(8.dp)
                        )
                    }
                }

                if (page == moviePageIndex) {

                    items(movieList) { movie ->
                        MovieItem(movie, navigateToMovieDetails)
                    }


                    when (movieList.loadState.append) {
                        is LoadState.Error -> {
                            onMovieEvent(SearchMovieEvent.Loading(false))
                            onMovieEvent(SearchMovieEvent.ErrorChanged("Something is wrong somewhere"))
                        }

                        is LoadState.Loading -> {
                            onMovieEvent(SearchMovieEvent.Loading(true))
                            item {
                                PagerProgressIndicator()
                            }
                        }

                        is LoadState.NotLoading -> {
                            onMovieEvent(SearchMovieEvent.Loading(false))
                        }
                    }
                }

                if (page == seriesPageIndex) {

                    items(seriesList) { series ->
                        SeriesItem(series, navigateToSeriesDetails)
                    }

                    when (seriesList.loadState.append) {
                        is LoadState.Error -> {
                            onSeriesEvent(SearchSeriesEvent.Loading(false))
                            onSeriesEvent(SearchSeriesEvent.ErrorChanged("Something is wrong somewhere"))
                        }

                        is LoadState.Loading -> {
                            onSeriesEvent(SearchSeriesEvent.Loading(true))
                            item {
                                PagerProgressIndicator()
                            }
                        }

                        is LoadState.NotLoading -> {
                            onSeriesEvent(SearchSeriesEvent.Loading(false))
                        }
                    }
                }


            }

        }
    }

}

@Composable
fun MovieItem(
    movie: SearchMovieResult,
    navigateToDetails: (id: Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(10.dp, 8.dp, 10.dp, 8.dp)
            .fillMaxWidth()
            .clickable {
                navigateToDetails(movie.id)
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
                            .padding(8.dp),
                        model = "https://image.tmdb.org/t/p/w500/${movie.poster_path}",
                        contentDescription = "${movie.title} Poster"
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
                            text = movie.title,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "Released: ${movie.release_date}",
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "Original language: ${movie.original_language.uppercase()}",
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "Average Rating: ${movie.vote_average}",
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }

            Text(
                text = movie.overview,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}


@Composable
fun SeriesItem(
    series: SearchSeriesResult,
    navigateToDetails: (id: Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(10.dp, 8.dp, 10.dp, 8.dp)
            .fillMaxWidth()
            .clickable {
                navigateToDetails(series.id)
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
                            .padding(8.dp),
                        model = "https://image.tmdb.org/t/p/w500/${series.poster_path}",
                        contentDescription = "${series.name} Poster"
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
                            text = series.name,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "First Aired ${series.first_air_date}",
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "Original Language: ${series.original_language.uppercase()}",
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            text = "Average Rating: ${series.vote_average}",
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }

            Text(
                text = series.overview,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
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
        contentAlignment = Alignment.Center
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

@Composable
fun PagerProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(42.dp)
                .padding(8.dp),
            strokeWidth = 5.dp
        )
    }
}

fun <T : Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    key: ((T) -> Any)? = null,
    contentType: ((T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit,
) {
    items(
        items.itemCount,
        key = items.itemKey(key),
        contentType = items.itemContentType(contentType)
    ) loop@{ i ->
        val item = items[i] ?: return@loop
        itemContent(item)
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

@Preview(showBackground = true)
@Composable
fun SearchSeriesScreenPreview() {
    SearchSeriesScreen(
        searchMovieState = SearchMovieState(
            "",
            null,
            false,
            flow {
                listOf(
                    SearchMovieResult(
                        true,
                        "",
                        emptyList(),
                        0,
                        "En-UK",
                        "",
                        "Lorem Ipson",
                        0.0,
                        "",
                        "2023-12-30",
                        "Movie Title",
                        false,
                        5.6,
                        0
                    ),
                    SearchMovieResult(
                        true,
                        "",
                        emptyList(),
                        0,
                        "En-UK",
                        "",
                        "Lorem Ipson",
                        0.0,
                        "",
                        "2023-12-30",
                        "Movie Title 2",
                        false,
                        5.6,
                        0
                    )
                )
            }
        ),
        searchSeriesState = SearchSeriesState(
            "",
            null,
            false,
            flow {
                listOf(
                    SearchSeriesResult(
                        true,
                        "",
                        "2023-12-30",
                        emptyList(),
                        0,
                        "En-UK",
                        emptyList(),
                        "En-US",
                        "Series Name",
                        "Lorem Ipson",
                        0.0,
                        "",
                        5.6,
                        0
                    ),
                    SearchSeriesResult(
                        true,
                        "",
                        "2023-12-30",
                        emptyList(),
                        0,
                        "En-UK",
                        emptyList(),
                        "En-US",
                        "Series Name 2",
                        "Lorem Ipson",
                        0.0,
                        "",
                        5.6,
                        0
                    )
                )
            }
        ),
        onMovieEvent = {},
        onSeriesEvent = {},
        navigateToMovieDetails = {},
        navigateToSeriesDetails = {}
    )
}

