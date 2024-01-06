package com.company.watchlist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.company.watchlist.data.domain.search.movie.SearchMovieResult
import com.company.watchlist.data.domain.search.series.SearchSeriesResult
import com.company.watchlist.data.remote.Resource
import com.company.watchlist.data.repositories.TMDBRepositoryImpl
import com.company.watchlist.presentation.appbar.AppBarEvent
import com.company.watchlist.presentation.appbar.AppBarState
import com.company.watchlist.presentation.details.movie.MovieDetailsState
import com.company.watchlist.presentation.details.series.SeriesDetailsState
import com.company.watchlist.presentation.search.movies.SearchMovieEvent
import com.company.watchlist.presentation.search.movies.SearchMovieState
import com.company.watchlist.presentation.search.series.SearchSeriesEvent
import com.company.watchlist.presentation.search.series.SearchSeriesState
import com.company.watchlist.presentation.trending.TrendingState
import com.company.watchlist.presentation.watchlist.WatchlistState
import com.company.watchlist.validation.ValidateSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TMDBViewModel @Inject constructor(
    private val repository: TMDBRepositoryImpl,
    private val validateSearch: ValidateSearch,
) : ViewModel() {

    val dynamicColours = repository.dynamicColours
    var appBarState = MutableStateFlow(AppBarState())
        private set
    var trendingState = MutableStateFlow(TrendingState())
        private set
    var searchMovieState = MutableStateFlow(SearchMovieState())
        private set
    var searchSeriesState = MutableStateFlow(SearchSeriesState())
        private set
    var movieDetailState = MutableStateFlow(MovieDetailsState())
        private set
    var seriesDetailState = MutableStateFlow(SeriesDetailsState())
        private set
    var watchlistState = MutableStateFlow(WatchlistState())
        private set


    fun onEvent(event: AppBarEvent) {
        when (event) {
            is AppBarEvent.AppbarTitleChanged -> {
                appBarState.update {
                    it.copy(title = event.title)
                }
            }
        }
    }

    fun onEvent(event: SearchMovieEvent) {
        when (event) {
            is SearchMovieEvent.Search -> searchMovie()
            is SearchMovieEvent.SearchQueryChanged -> {
                searchMovieState.update {
                    it.copy(query = event.query)
                }
            }
            is SearchMovieEvent.ErrorChanged -> {
                searchMovieState.update {
                    it.copy(searchError = event.error)
                }
            }

            is SearchMovieEvent.Loading -> {
                searchMovieState.update {
                    it.copy(
                        isLoading = true,
                        searchError = null
                    )
                }
            }
        }
    }

    fun onEvent(event: SearchSeriesEvent) {
        when (event) {
            is SearchSeriesEvent.Search -> searchSeries()
            is SearchSeriesEvent.SearchQueryChanged -> {
                searchSeriesState.update {
                    it.copy(query = event.query)
                }
            }
            is SearchSeriesEvent.ErrorChanged -> {
                searchSeriesState.update {
                    it.copy(searchError = event.error)
                }
            }

            is SearchSeriesEvent.Loading -> {
                searchSeriesState.update {
                    it.copy(
                        isLoading = true,
                        searchError = null
                    )
                }
            }
        }
    }

    fun getTrending() {
        viewModelScope.launch {

            trendingState.update {
                it.copy(isLoading = true, error = null)
            }

            when (val result = repository.getTrending()) {
                is Resource.Failure -> {
                    trendingState.update {
                        it.copy(
                            isLoading = false,
                            trendingList = emptyList(),
                            error = result.errorBody?.toString()
                        )
                    }
                }

                is Resource.Success -> {
                    trendingState.update {
                        it.copy(
                            isLoading = false,
                            trendingList = result.value.results,
                            error = null
                        )
                    }
                }
            }
        }
    }

    private fun searchMovie() {
        val query = searchMovieState.value.query
        val queryValidationResult = validateSearch.execute(query)

        val hasError = listOf(
            queryValidationResult
        ).any { !it.successful }

        searchMovieState.update { it.copy(searchError = queryValidationResult.errorMessage) }

        if (hasError)
            return


        viewModelScope.launch {

            searchMovieState.update {
                it.copy(isLoading = true)
            }

            val searchResult: Flow<PagingData<SearchMovieResult>> = repository.searchMovies(query)
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
//                .collectAsLazyPagingItems()

            searchMovieState.update {
                it.copy(isLoading = false, searchResult = searchResult)
            }

        }
    }

    private fun searchSeries() {
        val query = searchSeriesState.value.query
        val queryValidationResult = validateSearch.execute(query)

        val hasError = listOf(
            queryValidationResult
        ).any { !it.successful }

        searchSeriesState.update { it.copy(searchError = queryValidationResult.errorMessage) }

        if (hasError)
            return

        viewModelScope.launch {

            searchSeriesState.update {
                it.copy(isLoading = true)
            }

            val searchResult: Flow<PagingData<SearchSeriesResult>> = repository.searchSeries(query)
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
//                .collectAsLazyPagingItems()

            searchSeriesState.update {
                it.copy(isLoading = false, searchResult = searchResult)
            }

        }
    }

    fun getMovieDetails() {
        viewModelScope.launch {

            movieDetailState.update {
                it.copy(isLoading = true, error = null)
            }

            val movieId = movieDetailState.value.id

            when (val result = repository.getMovieDetails(movieId)) {
                is Resource.Failure -> {
                    movieDetailState.update {
                        it.copy(
                            isLoading = false,
                            error = result.errorBody?.toString()
                        )
                    }
                }

                is Resource.Success -> {
                    movieDetailState.update {
                        it.copy(
                            budget = result.value.budget,
                            genres = result.value.genres,
                            homepage = result.value.homepage,
                            id = result.value.id,
                            original_language = result.value.original_language,
                            original_title = result.value.original_title,
                            title = result.value.title,
                            popularity = result.value.popularity,
                            production_companies = result.value.production_companies,
                            release_date = result.value.release_date,
                            revenue = result.value.revenue,
                            runtime = result.value.runtime,
                            spoken_languages = result.value.spoken_languages,
                            status = result.value.status,
                            tagline = result.value.tagline,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
        }
    }

    fun getSeriesDetails() {
        viewModelScope.launch {

            seriesDetailState.update {
                it.copy(isLoading = true, error = null)
            }

            val seriesId = seriesDetailState.value.id

            when (val result = repository.getSeriesDetails(seriesId)) {
                is Resource.Failure -> {
                    seriesDetailState.update {
                        it.copy(
                            isLoading = false,
                            error = result.errorBody?.toString()
                        )
                    }
                }

                is Resource.Success -> {
                    seriesDetailState.update {
                        it.copy(
                            id = result.value.id,
                            firstAirDate = result.value.first_air_date,
                            lastAirDate = result.value.last_air_date,
                            homepage = result.value.homepage,
                            name = result.value.name,
                            originalName = result.value.original_name,
                            spokenLanguage = result.value.spoken_languages,
                            tagline = result.value.tagline,
                            popularity = result.value.popularity,
                            numberOfSeasons = result.value.number_of_seasons,
                            numberOfEpisodes = result.value.number_of_episodes,
                            lastEpisodeToAir = result.value.last_episode_to_air,
                            nextEpisodeToAir = result.value.next_episode_to_air,
                            seasons = result.value.seasons,
                            genres = result.value.genres,
                            languages = result.value.languages,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
        }
    }

}