package com.company.watchlist.di

import com.company.watchlist.data.repositories.TMDBRepository
import com.company.watchlist.data.repositories.TMDBRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTMDBRepository(
        tmdbRepositoryImpl: TMDBRepositoryImpl
    ): TMDBRepository

}