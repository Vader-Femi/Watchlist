package com.company.watchlist.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.company.watchlist.data.Film
import com.company.watchlist.data.ListType.FAVOURITESMOVIES
import com.company.watchlist.data.ListType.FAVOURITESSERIES
import com.company.watchlist.data.ListFields.ID
import com.company.watchlist.data.ListFields.NAME
import com.company.watchlist.data.ListFields.POSTERPATH
import com.company.watchlist.data.ListFields.AVERAGERATING
import com.company.watchlist.data.remote.Resource
import com.company.watchlist.data.remote.response.search.movie.SearchMovieResult
import com.company.watchlist.data.remote.response.search.series.SearchSeriesResult
import com.company.watchlist.data.repositories.watchlist.WatchlistRepositoryImpl
import com.company.watchlist.presentation.details.movie.MovieDetailsEvent
import com.company.watchlist.presentation.details.movie.MovieDetailsState
import com.company.watchlist.presentation.details.series.SeriesDetailsEvent
import com.company.watchlist.presentation.details.series.SeriesDetailsState
import com.company.watchlist.presentation.favourites.FavouritesEvent
import com.company.watchlist.presentation.favourites.FavouritesState
import com.company.watchlist.presentation.search.movies.SearchMovieEvent
import com.company.watchlist.presentation.search.movies.SearchMovieState
import com.company.watchlist.presentation.search.series.SearchSeriesEvent
import com.company.watchlist.presentation.search.series.SearchSeriesState
import com.company.watchlist.presentation.trending.TrendingEvent
import com.company.watchlist.presentation.trending.TrendingState
import com.company.watchlist.use_case.ValidateSearch
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repository: WatchlistRepositoryImpl,
    private val validateSearch: ValidateSearch,
) : BaseViewModel(repository) {

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
    var favouritesState = MutableStateFlow(FavouritesState())
        private set

    init {
        getTrending()
        getFavourites()
    }

    fun onEvent(event: TrendingEvent) {
        when (event) {
            is TrendingEvent.GetTrending -> {
                getTrending()
            }

            is TrendingEvent.DismissError -> {
                trendingState.update {
                    it.copy(error = null)
                }
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

            is MovieDetailsEvent.AddToFavourites -> addMovieToFavourites()
            is MovieDetailsEvent.DismissError -> {
                movieDetailState.update {
                    it.copy(error = null)
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

            is SeriesDetailsEvent.AddToFavourites -> addSeriesToFavourites()
            is SeriesDetailsEvent.DismissError -> {
                seriesDetailState.update {
                    it.copy(error = null)
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
                        isLoading = event.loading,
                        searchError = null
                    )
                }
            }

            is SearchMovieEvent.DismissError -> {
                searchMovieState.update {
                    it.copy(searchError = null)
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
                        isLoading = event.loading,
                        searchError = null
                    )
                }
            }

            is SearchSeriesEvent.DismissError -> {
                searchSeriesState.update {
                    it.copy(searchError = null)
                }
            }
        }
    }

    fun onEvent(event: FavouritesEvent) {
        when (event) {
            is FavouritesEvent.GetList -> getFavourites()
            is FavouritesEvent.IsLoadingChanged -> {
                favouritesState.update {
                    it.copy(isLoading = event.isLoading)
                }
            }

            is FavouritesEvent.RemoveFromFavourites -> {
                removeFromFavourites(event.film)
            }

            is FavouritesEvent.DismissError -> {
                favouritesState.update {
                    it.copy(error = null)
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
                    if (result.isNetworkError == true) {
                        trendingState.update {
                            it.copy(
                                isLoading = false,
                                trendingList = emptyList(),
                                error = "Check your network"
                            )
                        }
                    } else {
                        trendingState.update {
                            it.copy(
                                isLoading = false,
                                trendingList = emptyList(),
                                error = result.errorBody?.toString() ?: "Something went wrong 😪"
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

    private fun searchSeries() {
        val query = searchSeriesState.value.query.trim()
        val queryValidationResult = validateSearch.execute(query)

        val hasError = listOf(
            queryValidationResult
        ).any { !it.successful }

        searchSeriesState.update { it.copy(searchError = queryValidationResult.errorMessage) }

        if (hasError)
            return

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

    private fun getMovieDetails() {
        viewModelScope.launch {

            movieDetailState.update {
                MovieDetailsState(id = it.id, isLoading = true, error = null)
            }

            val movieId = movieDetailState.value.id

            if (movieId == null) {
                movieDetailState.update {
                    it.copy(isLoading = false, error = "Cannot get movie id")
                }
                return@launch
            }

            when (val result = repository.getMovieDetails(movieId)) {
                is Resource.Failure -> {
                    if (result.isNetworkError == true) {
                        movieDetailState.update {
                            it.copy(
                                isLoading = false,
                                error = "Check your network"
                            )
                        }
                    } else {
                        movieDetailState.update {
                            it.copy(
                                isLoading = false,
                                error = result.errorBody?.toString() ?: "Something went wrong 😪"
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
                            id = result.value.id.toLong(),
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
                SeriesDetailsState(id = it.id, isLoading = true, error = null)
            }

            val seriesId = seriesDetailState.value.id

            if (seriesId == null) {
                seriesDetailState.update {
                    it.copy(isLoading = false, error = "Cannot get tv series id")
                }
                return@launch
            }


            when (val result = repository.getSeriesDetails(seriesId)) {
                is Resource.Failure -> {
                    if (result.isNetworkError == true) {
                        seriesDetailState.update {
                            it.copy(
                                isLoading = false,
                                error = "Check your network"
                            )
                        }
                    } else {
                        seriesDetailState.update {
                            it.copy(
                                isLoading = false,
                                error = result.errorBody?.toString() ?: "Something went wrong 😪"
                            )
                        }
                    }
                }

                is Resource.Success -> {
                    seriesDetailState.update {
                        it.copy(
                            id = result.value.id.toLong(),
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

    private fun getFavourites() {

        val movieCollectionList = mutableMapOf<String, String>()
        val seriesCollectionList = mutableMapOf<String, String>()

        favouritesState.update {
            it.copy(isLoading = true, error = null)
        }

        repository.getFirestoreReference()
            .collection(repository.getAuthReference().uid.toString())
            .document(FAVOURITESMOVIES.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    for (data in document.data!!) {
                        movieCollectionList[data.key] = data.value.toString()
                    }
                    getFavouriteMovies(movieCollectionList)
                }
            }.addOnFailureListener { exception ->
                favouritesState.update {
                    it.copy(
                        isLoading = false,
                        error = "Cannot get favourite movies - ${exception.message}"
                    )
                }
            }

        repository.getFirestoreReference()
            .collection(repository.getAuthReference().uid.toString())
            .document(FAVOURITESSERIES.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    for (data in document.data!!) {
                        seriesCollectionList[data.key] = data.value.toString()
                    }
                    getFavouriteSeries(seriesCollectionList)
                }
            }.addOnFailureListener { exception ->
                favouritesState.update {
                    it.copy(
                        isLoading = false,
                        error = "Cannot get favourite series - ${exception.message}"
                    )
                }
            }

    }

    private fun getFavouriteMovies(movieCollectionList: MutableMap<String, String>) {
        favouritesState.update {
            it.copy(favouritesMoviesList = emptyList())
        }

        val movieList = mutableListOf<Film>()

        for (col in movieCollectionList) {
            repository.getFirestoreReference()
                .collection(repository.getAuthReference().uid.toString())
                .document(FAVOURITESMOVIES.toString())
                .collection(col.key)
                .document(col.value)
                .get()
                .addOnSuccessListener { document ->
                    if (document.data != null) {
                        movieList.add(
                            Film(
                                id = document.data!![ID.toString()]?.toString()?.toLong() ?: -1L,
                                name = document.data!![NAME.toString()]?.toString() ?: "",
                                posterPath = document.data!![POSTERPATH.toString()]?.toString(),
                                averageRating = document.data!![AVERAGERATING.toString()]?.toString()
                                    ?.toDouble(),
                                listType = FAVOURITESMOVIES
                            )
                        )
                        favouritesState.update {
                            it.copy(
                                favouritesMoviesList = movieList
                            )
                        }
                    }
                    favouritesState.update {
                        it.copy(isLoading = false, error = null)
                    }
                }.addOnFailureListener { exception ->
                    favouritesState.update {
                        it.copy(
                            isLoading = false,
                            error = "Cannot get favourite movies - ${exception.message}"
                        )
                    }
                }
        }

    }

    private fun getFavouriteSeries(seriesCollectionList: MutableMap<String, String>) {
        favouritesState.update {
            it.copy(favouritesSeriesList = emptyList())
        }

        val seriesList = mutableListOf<Film>()

        for (col in seriesCollectionList) {
            repository.getFirestoreReference()
                .collection(repository.getAuthReference().uid.toString())
                .document(FAVOURITESSERIES.toString())
                .collection(col.key)
                .document(col.value)
                .get()
                .addOnSuccessListener { document ->
                    if (document.data != null) {
                        seriesList.add(
                            Film(
                                id = document.data!![ID.toString()]?.toString()?.toLong() ?: -1L,
                                name = document.data!![NAME.toString()]?.toString() ?: "",
                                posterPath = document.data!![POSTERPATH.toString()]?.toString(),
                                averageRating = document.data!![AVERAGERATING.toString()]?.toString()
                                    ?.toDouble(),
                                listType = FAVOURITESSERIES
                            )
                        )
                        favouritesState.update {
                            it.copy(
                                favouritesSeriesList = seriesList
                            )
                        }
                    }
                    favouritesState.update {
                        it.copy(isLoading = false, error = null)
                    }
                }.addOnFailureListener { exception ->
                    favouritesState.update {
                        it.copy(
                            isLoading = false,
                            error = "Cannot get favourite series - ${exception.message}"
                        )
                    }
                }
        }

    }

    private fun addMovieToFavourites() {

        val movieId = movieDetailState.value.id
        val movieName = movieDetailState.value.title
        val posterPath: String? = movieDetailState.value.posterPath
        val averageRating: Double? = movieDetailState.value.voteAverage

        if (movieId == null || movieName.isNullOrEmpty()) {
            movieDetailState.update {
                it.copy(error = "No movie to add")
            }
            return
        }

        movieDetailState.update {
            it.copy(isLoading = true)
        }


        repository.getFirestoreReference()
            .collection(repository.getAuthReference().uid.toString())
            .document(FAVOURITESMOVIES.toString())
            .collection(movieName)
            .document(movieId.toString())
            .set(
                mapOf(
                    ID.toString() to movieId,
                    NAME.toString() to movieName,
                    POSTERPATH.toString() to posterPath,
                    AVERAGERATING.toString() to averageRating
                )
            )
            .addOnSuccessListener {

            }.addOnFailureListener { exception ->
                movieDetailState.update {
                    it.copy(
                        error = "Unable to add $movieName to favourites - ${exception.message}",
                        isLoading = false
                    )
                }
            }

        val collectionMap = mutableMapOf<String, Any>()

        repository.getFirestoreReference()
            .collection(repository.getAuthReference().uid.toString())
            .document(FAVOURITESMOVIES.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    for (data in document.data!!) {
                        collectionMap[data.key] = data.value
                    }
                }
                collectionMap[movieName] = movieId
                addMoviesFavOuterDoc(collectionMap)
            }.addOnFailureListener { exception ->
                movieDetailState.update {
                    it.copy(
                        error = "Unable to add $movieName to favourites - ${exception.message}",
                        isLoading = false
                    )
                }
            }
    }

    private fun addMoviesFavOuterDoc(collectionMap: MutableMap<String, Any>) {
        repository.getFirestoreReference()
            .collection(repository.getAuthReference().uid.toString())
            .document(FAVOURITESMOVIES.toString())
            .set(collectionMap)
            .addOnSuccessListener {
                getFavourites()
                movieDetailState.update {
                    it.copy(isLoading = false, error = null)
                }
            }.addOnFailureListener { exception ->
                movieDetailState.update {
                    it.copy(
                        error = "Unable to add movie to favourites - ${exception.message}",
                        isLoading = false
                    )
                }
            }
    }

    private fun addSeriesToFavourites() {
        val seriesId = seriesDetailState.value.id
        val seriesName = seriesDetailState.value.name
        val posterPath: String? = seriesDetailState.value.posterPath
        val averageRating: Double? = seriesDetailState.value.voteAverage

        if (seriesId == null || seriesName.isNullOrEmpty()) {
            seriesDetailState.update {
                it.copy(error = "No series to add")
            }
            return
        }

        seriesDetailState.update {
            it.copy(isLoading = true)
        }


        repository.getFirestoreReference()
            .collection(repository.getAuthReference().uid.toString())
            .document(FAVOURITESSERIES.toString())
            .collection(seriesName)
            .document(seriesId.toString())
            .set(
                mapOf(
                    ID.toString() to seriesId,
                    NAME.toString() to seriesName,
                    POSTERPATH.toString() to posterPath,
                    AVERAGERATING.toString() to averageRating
                )
            )
            .addOnSuccessListener {

            }.addOnFailureListener { exception ->
                seriesDetailState.update {
                    it.copy(
                        error = "Unable to add $seriesName to favourites - ${exception.message}",
                        isLoading = false
                    )
                }
            }

        val collectionMap = mutableMapOf<String, Any>()

        repository.getFirestoreReference()
            .collection(repository.getAuthReference().uid.toString())
            .document(FAVOURITESSERIES.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    for (data in document.data!!) {
                        collectionMap[data.key] = data.value
                    }
                }
                collectionMap[seriesName] = seriesId
                addSeriesFavOuterDoc(collectionMap)
            }.addOnFailureListener { exception ->
                seriesDetailState.update {
                    it.copy(
                        error = "Unable to add $seriesName to favourites - ${exception.message}",
                        isLoading = false
                    )
                }
            }

    }

    private fun addSeriesFavOuterDoc(collectionMap: MutableMap<String, Any>) {
        repository.getFirestoreReference()
            .collection(repository.getAuthReference().uid.toString())
            .document(FAVOURITESSERIES.toString())
            .set(collectionMap)
            .addOnSuccessListener {
                getFavourites()
                seriesDetailState.update {
                    it.copy(isLoading = false, error = null)
                }
            }.addOnFailureListener { exception ->
                seriesDetailState.update {
                    it.copy(
                        error = "Unable to add series to favourites - ${exception.message}",
                        isLoading = false
                    )
                }
            }
    }

    private fun removeFromFavourites(
        film: Film,
    ) {

        favouritesState.update {
            it.copy(isLoading = true)
        }

        when (film.listType) {
            FAVOURITESMOVIES -> {

                repository.getFirestoreReference()
                    .collection(repository.getAuthReference().uid.toString())
                    .document(FAVOURITESMOVIES.toString())
                    .collection(film.name)
                    .document(film.id.toString())
                    .delete()
                    .addOnSuccessListener {


                        repository.getFirestoreReference()
                            .collection(repository.getAuthReference().uid.toString())
                            .document(FAVOURITESMOVIES.toString())
                            .update(mapOf(film.name to FieldValue.delete()))
                            .addOnSuccessListener {
                                getFavourites()
                                favouritesState.update {
                                    it.copy(isLoading = false, error = null)
                                }
                            }.addOnFailureListener { exception ->
                                favouritesState.update {
                                    it.copy(
                                        error = "Unable to remove ${film.name} from favourites - ${exception.message}",
                                        isLoading = false
                                    )
                                }
                            }


                    }.addOnFailureListener { exception ->
                        movieDetailState.update {
                            it.copy(
                                error = "Unable to add ${film.name} to favourites - ${exception.message}",
                                isLoading = false
                            )
                        }
                    }
            }

            FAVOURITESSERIES -> {

                repository.getFirestoreReference()
                    .collection(repository.getAuthReference().uid.toString())
                    .document(FAVOURITESSERIES.toString())
                    .collection(film.name)
                    .document(film.id.toString())
                    .delete()
                    .addOnSuccessListener {


                        repository.getFirestoreReference()
                            .collection(repository.getAuthReference().uid.toString())
                            .document(FAVOURITESSERIES.toString())
                            .update(mapOf(film.name to FieldValue.delete()))
                            .addOnSuccessListener {
                                favouritesState.update {
                                    it.copy(isLoading = false, error = null)
                                }
                            }.addOnFailureListener { exception ->
                                favouritesState.update {
                                    it.copy(
                                        error = "Unable to remove ${film.name} from favourites - ${exception.message}",
                                        isLoading = false
                                    )
                                }
                            }


                    }.addOnFailureListener { exception ->
                        seriesDetailState.update {
                            it.copy(
                                error = "Unable to add ${film.name} to favourites - ${exception.message}",
                                isLoading = false
                            )
                        }
                    }


            }
        }

    }

}