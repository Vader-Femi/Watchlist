package com.company.watchlist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.watchlist.data.repositories.base.BaseRepositoryImpl
import com.company.watchlist.presentation.appbar.AppBarEvent
import com.company.watchlist.presentation.appbar.AppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val repository: BaseRepositoryImpl,
) : ViewModel() {

    var appBarState = MutableStateFlow(AppBarState())
        private set

    fun onEvent(event: AppBarEvent) {
        when (event) {
            is AppBarEvent.AppbarChanged -> {
                appBarState.update {
                    it.copy(screen = event.screen)
                }
            }
        }
    }

    val useDynamicTheme = repository.useDynamicTheme

    fun logOutFromFirebaseAndDeleteFromPref(){
        viewModelScope.launch {
            repository.logOutFromFirebaseAndDeleteFromPref()
        }
    }
}
