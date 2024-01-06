package com.company.watchlist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.company.watchlist.data.Screen
import com.company.watchlist.presentation.details.movie.MovieDetailsState
import com.company.watchlist.presentation.details.series.SeriesDetailsState
import com.company.watchlist.presentation.search.movies.SearchMovieEvent
import com.company.watchlist.presentation.search.movies.SearchMovieState
import com.company.watchlist.presentation.search.series.SearchSeriesEvent
import com.company.watchlist.presentation.search.series.SearchSeriesState
import com.company.watchlist.presentation.trending.TrendingState
import com.company.watchlist.presentation.watchlist.WatchlistState
import com.company.watchlist.ui.details.MovieDetailsScreen
import com.company.watchlist.ui.details.SeriesDetailsScreen
import com.company.watchlist.ui.search.SearchSeriesScreen
import com.company.watchlist.ui.trending.TrendingScreen
import com.company.watchlist.ui.watchlist.WatchlistScreen
import com.company.watchlist.viewmodels.TMDBViewModel

@Composable
fun NavigationHost(
    trendingState: TrendingState,
    searchMovieState: SearchMovieState,
    searchSeriesState: SearchSeriesState,
    movieDetailsState: MovieDetailsState,
    seriesDetailsState: SeriesDetailsState,
    watchlistState: WatchlistState,
    viewModel: TMDBViewModel,
    appBarChanged: (String) -> Unit,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = Screen.TrendingScreen.route,
        route = Screen.NavigationRoute.route
    ) {

        composable(route = Screen.TrendingScreen.route) {
            TrendingScreen(trendingState) { viewModel.getTrending() }
            appBarChanged(Screen.TrendingScreen.name)
        }

        composable(route = Screen.SearchScreen.route) {
            SearchSeriesScreen(
                searchMovieState,
                searchSeriesState,
                { viewModel.onEvent(SearchMovieEvent.Search) },
                { viewModel.onEvent(SearchSeriesEvent.Search) }
            )
            appBarChanged(Screen.SearchScreen.name)
        }

        composable(
            route = Screen.MovieDetailsScreen.route + "?movie_id={movie_id}",
            arguments = listOf(
                navArgument("movie_id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { entry ->
            MovieDetailsScreen(
                movieId = entry.arguments?.getInt("movie_id") ?: -1,
                state = movieDetailsState
            ) { viewModel.getMovieDetails() }
            appBarChanged(Screen.MovieDetailsScreen.name)
        }

        composable(
            route = Screen.SeriesDetailsScreen.route + "?series_id={series_id}",
            arguments = listOf(
                navArgument("series_id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { entry ->
            SeriesDetailsScreen(
                seriesId = entry.arguments?.getInt("series_id") ?: -1,
                state = seriesDetailsState
            ) { viewModel.getSeriesDetails() }
            appBarChanged(Screen.SeriesDetailsScreen.name)
        }

        composable(route = Screen.WatchlistScreen.route) {
            WatchlistScreen(watchlistState) {}
            appBarChanged(Screen.WatchlistScreen.name)
        }

    }
}