package com.company.watchlist.ui.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.company.watchlist.presentation.favourites.FavouritesState
import com.company.watchlist.ui.components.ErrorAlertDialog

@ExperimentalMaterialApi
@Composable
fun FavouritesScreen(
    state: FavouritesState,
    getFavouritesList: () -> Unit
){

    val scrollState = rememberScrollState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = getFavouritesList
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
            .pullRefresh(pullRefreshState)
    ) {

        if (state.error != null) {
            ErrorAlertDialog(state.error)
        }

    }

}