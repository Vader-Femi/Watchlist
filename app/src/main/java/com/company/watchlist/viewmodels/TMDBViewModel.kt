package com.company.watchlist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.company.watchlist.data.remote.Resource
import com.company.watchlist.data.remote.response.search.movie.SearchMovieResult
import com.company.watchlist.data.remote.response.search.series.SearchSeriesResult
import com.company.watchlist.data.repositories.TMDBRepositoryImpl
import com.company.watchlist.presentation.appbar.AppBarEvent
import com.company.watchlist.presentation.appbar.AppBarState
import com.company.watchlist.presentation.details.movie.MovieDetailsEvent
import com.company.watchlist.presentation.details.movie.MovieDetailsState
import com.company.watchlist.presentation.details.series.SeriesDetailsEvent
import com.company.watchlist.presentation.details.series.SeriesDetailsState
import com.company.watchlist.presentation.search.movies.SearchMovieEvent
import com.company.watchlist.presentation.search.movies.SearchMovieState
import com.company.watchlist.presentation.search.series.SearchSeriesEvent
import com.company.watchlist.presentation.search.series.SearchSeriesState
import com.company.watchlist.presentation.trending.TrendingEvent
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

    fun onEvent(event: TrendingEvent) {
        when (event) {
            is TrendingEvent.GetTrending -> {
                getTrending()
            }
        }
    }
    fun onEvent(event: MovieDetailsEvent) {
        when (event) {
            is MovieDetailsEvent.GetDetails -> {
                getMovieDetails()
            }
            is MovieDetailsEvent.SetId -> {
                movieDetailState.update {
                    it.copy(id = event.id)
                }
            }
        }
    }
    fun onEvent(event: SeriesDetailsEvent) {
        when (event) {
            is SeriesDetailsEvent.GetDetails -> {
                getSeriesDetails()
            }
            is SeriesDetailsEvent.SetId -> {
                seriesDetailState.update {
                    it.copy(id = event.id)
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

    private fun getTrending() {
        viewModelScope.launch {

            trendingState.update {
                it.copy(isLoading = true, error = null)
            }

            when (val result = repository.getTrending()) {
                is Resource.Failure -> {
                    if (result.isNetworkError == true){
                        trendingState.update {
                            it.copy(
                                isLoading = false,
                                trendingList = emptyList(),
                                error = "Check your network"
                            )
                        }
                    } else{
                        trendingState.update {
                            it.copy(
                                isLoading = false,
                                trendingList = emptyList(),
                                error = result.errorBody?.toString() ?: "Something went wrong"
                            )
                        }
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
        val query = searchMovieState.value.query.trim()
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
        val query = searchSeriesState.value.query.trim()
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

    private fun getMovieDetails() {
        viewModelScope.launch {

            movieDetailState.update {
                it.copy(isLoading = true, error = null)
            }

            val movieId = movieDetailState.value.id

            if (movieId == null){
                movieDetailState.update {
                    it.copy(isLoading = false, error = null)
                }
                return@launch
            }

            when (val result = repository.getMovieDetails(movieId)) {
                is Resource.Failure -> {
                    if (result.isNetworkError == true){
                        movieDetailState.update {
                            it.copy(
                                isLoading = false,
                                error = "Check your network"
                            )
                        }
                    } else{
                        movieDetailState.update {
                            it.copy(
                                isLoading = false,
                                error = result.errorBody?.toString() ?: "Something went wrong"
                            )
                        }
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
                            overview = result.value.overview,
                            title = result.value.title,
                            posterPath = result.value.poster_path,
                            voteAverage = result.value.vote_average,
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

    private fun getSeriesDetails() {
        viewModelScope.launch {

            seriesDetailState.update {
                it.copy(isLoading = true, error = null)
            }

            val seriesId = seriesDetailState.value.id

            if (seriesId == null){
                seriesDetailState.update {
                    it.copy(isLoading = false, error = null)
                }
                return@launch
            }

            when (val result = repository.getSeriesDetails(seriesId)) {
                is Resource.Failure -> {
                    if (result.isNetworkError == true){
                        seriesDetailState.update {
                            it.copy(
                                isLoading = false,
                                error = "Check your network"
                            )
                        }
                    } else{
                        seriesDetailState.update {
                            it.copy(
                                isLoading = false,
                                error = result.errorBody?.toString() ?: "Something went wrong"
                            )
                        }
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
                            originalLanguage = result.value.original_language,
                            overview = result.value.overview,
                            spokenLanguage = result.value.spoken_languages,
                            posterPath = result.value.poster_path,
                            tagline = result.value.tagline,
                            voteAverage = result.value.vote_average,
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