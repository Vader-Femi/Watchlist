package com.company.watchlist.viewmodels

import androidx.lifecycle.viewModelScope
import com.company.watchlist.data.repositories.main_activity.MainActivityRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: MainActivityRepositoryImpl,
) : BaseViewModel(repository) {

    suspend fun isUserNew(): Boolean = repository.isNewUser()


    private val _isLoading = MutableStateFlow(true)
    val isLoading: Boolean
        get() = _isLoading.value

    init {
        viewModelScope.launch {
            delay(1500)
            _isLoading.value = false
        }
    }
}