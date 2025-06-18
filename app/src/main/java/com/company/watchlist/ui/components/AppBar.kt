package com.company.watchlist.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import com.company.watchlist.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    screen: Screen,
    scrollBehavior: TopAppBarScrollBehavior,
    backArrow: () -> Unit,
) {

    when (screen) {
        Screen.AuthenticationRoute -> {}

        Screen.FavouritesScreen -> {
            LargeAppBar(screen = screen, backArrow = null, scrollBehavior = scrollBehavior)
        }

        Screen.LogInScreen -> {
            LargeAppBar(screen = screen, backArrow = null, scrollBehavior = scrollBehavior)
        }

        Screen.MovieDetailsScreen -> {
            TopAppBar(screen = screen, backArrow = null)
        }

        Screen.OnBoardingScreen -> {}

        Screen.ResetPasswordScreen -> {
            LargeAppBar(screen = screen, backArrow = backArrow, scrollBehavior = scrollBehavior)
        }

        Screen.SearchScreen -> {
            LargeAppBar(screen = screen, backArrow = null, scrollBehavior = scrollBehavior)
        }

        Screen.SeriesDetailsScreen -> {
            TopAppBar(screen = screen, backArrow = null)
        }

        Screen.SignUpScreen -> {
            LargeAppBar(screen = screen, backArrow = null, scrollBehavior = scrollBehavior)
        }

        Screen.TrendingScreen -> {
            LargeAppBar(screen = screen, backArrow = null, scrollBehavior = scrollBehavior)
        }

        Screen.ProfileScreen -> {
            LargeAppBar(screen = screen, backArrow = null, scrollBehavior = scrollBehavior)
        }

        Screen.WatchlistRoute -> {}
    }

}