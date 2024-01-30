package com.company.watchlist.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.company.watchlist.presentation.appbar.AppBarEvent
import com.company.watchlist.presentation.details.movie.MovieDetailsState
import com.company.watchlist.presentation.details.series.SeriesDetailsState
import com.company.watchlist.presentation.search.movies.SearchMovieState
import com.company.watchlist.presentation.search.series.SearchSeriesState
import com.company.watchlist.presentation.trending.TrendingState
import com.company.watchlist.presentation.favourites.FavouritesState
import com.company.watchlist.ui.details.MovieDetailsScreen
import com.company.watchlist.ui.details.SeriesDetailsScreen
import com.company.watchlist.ui.search.SearchSeriesScreen
import com.company.watchlist.ui.trending.TrendingScreen
import com.company.watchlist.ui.favourites.FavouritesScreen
import com.company.watchlist.viewmodels.WatchlistViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WatchlistNavigationHost(
    trendingState: TrendingState,
    searchMovieState: SearchMovieState,
    searchSeriesState: SearchSeriesState,
    movieDetailsState: MovieDetailsState,
    seriesDetailsState: SeriesDetailsState,
    favouritesState: FavouritesState,
    viewModel: WatchlistViewModel,
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.TrendingScreen.route,
        route = Screen.WatchlistRoute.route
    ) {

        composable(route = Screen.TrendingScreen.route) {
            TrendingScreen(
                state = trendingState,
                trendingEvent = {viewModel.onEvent(it)},
                navigateToMovieDetails = {
                    navController.navigate(
                        Screen.MovieDetailsScreen.withArgs(
                            Pair("movie_id", it)
                        )
                    ) {
                        popUpTo(Screen.TrendingScreen.route)
                        launchSingleTop = true
                    }
                },
                navigateToSeriesDetails = {
                    navController.navigate(
                        Screen.SeriesDetailsScreen.withArgs(
                            Pair("series_id", it)
                        )
                    ) {
                        popUpTo(Screen.TrendingScreen.route)
                        launchSingleTop = true
                    }
                }
            )
            viewModel.onEvent(
                AppBarEvent.AppbarChanged(
                    Screen.TrendingScreen
                )
            )
        }

        composable(route = Screen.SearchScreen.route) {
            SearchSeriesScreen(
                searchMovieState = searchMovieState,
                searchSeriesState = searchSeriesState,
                onMovieEvent = { viewModel.onEvent(it) },
                onSeriesEvent = { viewModel.onEvent(it) },
                navigateToMovieDetails = {
                    navController.navigate(
                        Screen.MovieDetailsScreen.withArgs(
                            Pair("movie_id", it)
                        )
                    ) {
                        popUpTo(Screen.SearchScreen.route)
                        launchSingleTop = true
                    }
                },
                navigateToSeriesDetails = {
                    navController.navigate(
                        Screen.SeriesDetailsScreen.withArgs(
                            Pair("series_id", it)
                        )
                    ) {
                        popUpTo(Screen.SearchScreen.route)
                        launchSingleTop = true
                    }
                }
            )
            viewModel.onEvent(
                AppBarEvent.AppbarChanged(
                    Screen.SearchScreen
                )
            )
        }

        composable(
            route = Screen.MovieDetailsScreen.route + "?movie_id={movie_id}",
            arguments = listOf(
                navArgument("movie_id") {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                }
            )
        ) { entry ->
            MovieDetailsScreen(
                movieId = entry.arguments?.getInt("movie_id") ?: -1,
                state = movieDetailsState
            ) { viewModel.onEvent(it) }
            viewModel.onEvent(
                AppBarEvent.AppbarChanged(
                    Screen.MovieDetailsScreen
                )
            )
        }

        composable(
            route = Screen.SeriesDetailsScreen.route + "?series_id={series_id}",
            arguments = listOf(
                navArgument("series_id") {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                }
            )
        ) { entry ->
            SeriesDetailsScreen(
                seriesId = entry.arguments?.getInt("series_id") ?: -1,
                state = seriesDetailsState
            ) { viewModel.onEvent(it) }
            viewModel.onEvent(
                AppBarEvent.AppbarChanged(
                    Screen.SeriesDetailsScreen
                )
            )
        }

        composable(route = Screen.FavouritesScreen.route) {
            FavouritesScreen(
                state = favouritesState,
                onEvent = {viewModel.onEvent(it)},
                navigateToMovieDetails = {
                    navController.navigate(
                        Screen.MovieDetailsScreen.withArgs(
                            Pair("movie_id", it)
                        )
                    ) {
                        popUpTo(Screen.SearchScreen.route)
                        launchSingleTop = true
                    }
                },
                navigateToSeriesDetails = {
                    navController.navigate(
                        Screen.SeriesDetailsScreen.withArgs(
                            Pair("series_id", it)
                        )
                    ) {
                        popUpTo(Screen.SearchScreen.route)
                        launchSingleTop = true
                    }
                } )
            viewModel.onEvent(
                AppBarEvent.AppbarChanged(
                    Screen.FavouritesScreen
                )
            )
        }

    }
}