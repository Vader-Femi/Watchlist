package com.company.watchlist.di

import android.app.Application
import com.company.watchlist.data.remote.RetrofitBuilder
import com.company.watchlist.data.remote.TMDBApi
import com.company.watchlist.data.repositories.TMDBRepository
import com.company.watchlist.data.repositories.TMDBRepositoryImpl
import com.company.watchlist.validation.ValidateSearch
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): TMDBApi {
        return RetrofitBuilder.api
    }

    @Provides
    @Singleton
    fun provideTMDBRepository(api: TMDBApi): TMDBRepository {
        return TMDBRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideValidateSearch(application: Application): ValidateSearch {
        return ValidateSearch(application)
    }
}