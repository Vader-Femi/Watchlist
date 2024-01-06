package com.company.watchlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.company.watchlist.data.BottomNavBarData
import com.company.watchlist.data.Screen
import com.company.watchlist.navigation.NavigationHost
import com.company.watchlist.presentation.appbar.AppBarEvent
import com.company.watchlist.ui.components.AppBar
import com.company.watchlist.ui.components.BottomNavigationBar
import com.company.watchlist.ui.theme.WatchlistTheme
import com.company.watchlist.viewmodels.TMDBViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.Calendar
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: TMDBViewModel = hiltViewModel()
            val navController = rememberNavController()
            val appBarState by viewModel.appBarState.collectAsStateWithLifecycle()
            val trendingState by viewModel.trendingState.collectAsStateWithLifecycle()
            val searchMovieState by viewModel.searchMovieState.collectAsStateWithLifecycle()
            val searchSeriesState by viewModel.searchSeriesState.collectAsStateWithLifecycle()
            val movieDetailState by viewModel.movieDetailState.collectAsStateWithLifecycle()
            val seriesDetailState by viewModel.seriesDetailState.collectAsStateWithLifecycle()
            val watchlistState by viewModel.watchlistState.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = true) {
                delay(5.seconds)
                viewModel.onEvent(
                    AppBarEvent.AppbarTitleChanged(
                        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                            in 6..11 -> "Good Morning "
                            in 12..16 -> "Good Afternoon "
                            in 17..22 -> "Good Evening "
                            else -> "You should be sleeping "
                        }
                            .plus("Nosa")
                    )
                )
            }

            WatchlistTheme(dynamicColor = viewModel.dynamicColours) {
                Surface {
                    Scaffold(
                        topBar = {
                            AppBar(title = appBarState.title) {
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
                        }
                    ) {
                        Box(
                            modifier = Modifier.padding(it)
                        ) {
                            if (trendingState.isLoading || searchMovieState.isLoading ||
                                searchSeriesState.isLoading || movieDetailState.isLoading ||
                                seriesDetailState.isLoading || watchlistState.isLoading
                            ) {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            }
                            NavigationHost(
                                trendingState,
                                searchMovieState,
                                searchSeriesState,
                                movieDetailState,
                                seriesDetailState,
                                watchlistState,
                                viewModel = viewModel,
                                { appBarTitle ->
                                    viewModel.onEvent(
                                        AppBarEvent.AppbarTitleChanged(
                                            appBarTitle
                                        )
                                    )
                                },
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}
