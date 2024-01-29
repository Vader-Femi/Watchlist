package com.company.watchlist.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.company.watchlist.navigation.AuthenticationNavigation
import com.company.watchlist.ui.components.AppBar
import com.company.watchlist.ui.theme.WatchlistTheme
import com.company.watchlist.viewmodels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = hiltViewModel<AuthenticationViewModel>()
            val appBarState by viewModel.appBarState.collectAsStateWithLifecycle()
            val logInState by viewModel.logInState.collectAsStateWithLifecycle()
            val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()
            val resetPasswordState by viewModel.resetPasswordState.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            WatchlistTheme(dynamicColor = viewModel.useDynamicTheme) {
                Surface {
                    Scaffold(
                        topBar = {
                            AppBar(screen = appBarState.screen, scrollBehavior = scrollBehavior) {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        content = { paddingValue ->
                            Box(
                                modifier = Modifier
                                    .padding(paddingValue),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                AuthenticationNavigation(
                                    viewModel = viewModel,
                                    logInState = logInState,
                                    signUpState = signUpState,
                                    resetPasswordState = resetPasswordState,
                                    navController = navController
                                ){
                                    Intent(this@AuthenticationActivity, WatchlistActivity::class.java).also {
                                        it.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(it)
                                        finish()
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}