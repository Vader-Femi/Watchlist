package com.company.watchlist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.company.watchlist.presentation.appbar.AppBarEvent
import com.company.watchlist.presentation.login.LogInState
import com.company.watchlist.presentation.resetpassword.ResetPasswordState
import com.company.watchlist.presentation.signup.SignUpState
import com.company.watchlist.ui.authentication.onboarding.OnBoardingScreen
import com.company.watchlist.viewmodels.AuthenticationViewModel
import com.company.watchlist.ui.authentication.login.LogInScreen
import com.company.watchlist.ui.authentication.signup.SignUpScreen
import com.company.watchlist.ui.authentication.resetpassword.ResetPasswordScreen

@Composable
fun AuthenticationNavigation(
    viewModel: AuthenticationViewModel,
    logInState: LogInState,
    signUpState: SignUpState,
    resetPasswordState: ResetPasswordState,
    navController: NavHostController,
    toWatchlistActivity: () -> Unit,
) {

    NavHost(
        navController = navController,
        startDestination = Screen.OnBoardingScreen.route,
        route = Screen.AuthenticationRoute.route
    ) {

        composable(route = Screen.OnBoardingScreen.route) {
            OnBoardingScreen(
                toLogIn = { navController.navigate(Screen.LogInScreen.route) },
                toSignIn = { navController.navigate(Screen.SignUpScreen.route) })
            viewModel.onEvent(
                AppBarEvent.AppbarChanged(
                    Screen.OnBoardingScreen
                )
            )
        }

        composable(route = Screen.SignUpScreen.route) {
            SignUpScreen(
                state = signUpState,
                signUpChannelEvents = viewModel.signUpChannelEvents,
                onEvent = { viewModel.onEvent(it) },
                onSignUp = toWatchlistActivity,
                goToLogIn = {
                    navController.navigate(Screen.LogInScreen.route) {
                        popUpTo(Screen.OnBoardingScreen.route)
                        launchSingleTop = true
                    }
                }
            )
            viewModel.onEvent(
                AppBarEvent.AppbarChanged(
                    Screen.SignUpScreen
                )
            )
        }

        composable(route = Screen.LogInScreen.route) {
            LogInScreen(
                state = logInState,
                logInChannelEvents = viewModel.logInChannelEvents,
                onEvent = { viewModel.onEvent(it) },
                onLogIn = toWatchlistActivity,
                goToResetPassword = { navController.navigate(Screen.ResetPasswordScreen.route) },
                goToCreateAccount = {
                    navController.navigate(Screen.SignUpScreen.route) {
                        popUpTo(Screen.OnBoardingScreen.route)
                        launchSingleTop = true
                    }
                }
            )
            viewModel.onEvent(
                AppBarEvent.AppbarChanged(
                    Screen.LogInScreen
                )
            )
        }

        composable(route = Screen.ResetPasswordScreen.route) {
            ResetPasswordScreen(
                state = resetPasswordState,
                resetPasswordChannelEvents = viewModel.resetPasswordChannelEvents,
                onEvent = { viewModel.onEvent(it) },
                goBack = { navController.popBackStack() },
            )
            viewModel.onEvent(
                AppBarEvent.AppbarChanged(
                    Screen.ResetPasswordScreen
                )
            )
        }

    }
}