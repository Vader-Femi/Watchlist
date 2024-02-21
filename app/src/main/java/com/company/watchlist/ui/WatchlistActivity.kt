package com.company.watchlist.ui

import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.company.watchlist.data.BottomNavBarData
import com.company.watchlist.navigation.Screen
import com.company.watchlist.navigation.WatchlistNavigationHost
import com.company.watchlist.presentation.WatchlistEventChannel
import com.company.watchlist.presentation.details.movie.MovieDetailsEvent
import com.company.watchlist.presentation.details.series.SeriesDetailsEvent
import com.company.watchlist.ui.components.AppBar
import com.company.watchlist.ui.components.BottomNavigationBar
import com.company.watchlist.ui.theme.WatchlistTheme
import com.company.watchlist.viewmodels.WatchlistViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class WatchlistActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val context = LocalContext.current
            val viewModel: WatchlistViewModel = hiltViewModel()
            val navController = rememberNavController()
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            val appBarState by viewModel.appBarState.collectAsStateWithLifecycle()
            val trendingState by viewModel.trendingState.collectAsStateWithLifecycle()
            val searchMovieState by viewModel.searchMovieState.collectAsStateWithLifecycle()
            val searchSeriesState by viewModel.searchSeriesState.collectAsStateWithLifecycle()
            val movieDetailsState by viewModel.movieDetailState.collectAsStateWithLifecycle()
            val seriesDetailsState by viewModel.seriesDetailState.collectAsStateWithLifecycle()
            val favouritesState by viewModel.favouritesState.collectAsStateWithLifecycle()
            val watchlistChannelEvents = viewModel.watchlistChannelEvents


            LaunchedEffect(key1 = watchlistChannelEvents) {
                watchlistChannelEvents.collectLatest { event ->
                    when (event) {
                        is WatchlistEventChannel.AddedToFavourites -> {
                            Toast.makeText(context, event.addedToFavMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            LaunchedEffect(key1 = true) {
                delay(5.seconds)
                val greeting: String = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                    in 6..11 -> "Good Morning ${viewModel.getUserFName()}"
                    in 12..16 -> "Good Afternoon ${viewModel.getUserFName()}"
                    in 17..22 -> "Good Evening ${viewModel.getUserFName()}"
                    else -> "You should be sleeping ${viewModel.getUserFName()}" }

                Screen.TrendingScreen.name = greeting
//                Screen.FavouritesScreen.name = greeting

//                val newScreen = appBarState.screen
//                newScreen.name = greeting
//                viewModel.onEvent(
//                    AppBarEvent.AppbarChanged(newScreen)
//                )
            }

            WatchlistTheme(dynamicColor = viewModel.useDynamicTheme) {
                Surface {
                    Scaffold(
                        topBar = {
                            AppBar(screen = appBarState.screen, scrollBehavior = scrollBehavior) {
                                navController.popBackStack()
                            }
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                items = BottomNavBarData.getItems(),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route) {
                                        popUpTo(Screen.TrendingScreen.route)
                                        launchSingleTop = true
                                    }
                                }
                            )
                        },
                        floatingActionButton = {
                            when (appBarState.screen) {
                                is Screen.MovieDetailsScreen -> {
                                    ExtendedFloatingActionButton(
                                        text = { Text(text = "Add to favourite") },
                                        onClick = {
                                            viewModel.onEvent(
                                                MovieDetailsEvent.AddToFavourites
                                            )
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.TwoTone.FavoriteBorder,
                                                contentDescription = "Favourite Icon"
                                            )
                                        }
                                    )
                                }

                                is Screen.SeriesDetailsScreen -> {
                                    ExtendedFloatingActionButton(
                                        text = { Text(text = "Add to favourite") },
                                        onClick = {
                                            viewModel.onEvent(
                                                SeriesDetailsEvent.AddToFavourites
                                            )
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.TwoTone.FavoriteBorder,
                                                contentDescription = "Favourite Icon"
                                            )
                                        }
                                    )
                                }

                                else -> {}
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        content = { paddingValues ->
                            Box(
                                modifier = Modifier.padding(paddingValues),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                WatchlistNavigationHost(
                                    trendingState = trendingState,
                                    searchMovieState = searchMovieState,
                                    searchSeriesState = searchSeriesState,
                                    movieDetailsState = movieDetailsState,
                                    seriesDetailsState = seriesDetailsState,
                                    favouritesState = favouritesState,
                                    viewModel = viewModel,
                                    navController = navController,
                                )
                                AnimatedVisibility(
                                    visible = trendingState.isLoading || movieDetailsState.isLoading ||
                                            seriesDetailsState.isLoading || favouritesState.isLoading ||
                                            searchMovieState.isLoading || searchSeriesState.isLoading
                                ) {
                                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                }
                            }

                        }
                    )
                }
            }
        }
    }
}