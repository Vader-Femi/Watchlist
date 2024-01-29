package com.company.watchlist.di

import com.company.watchlist.data.repositories.authentication.AuthenticationRepository
import com.company.watchlist.data.repositories.authentication.AuthenticationRepositoryImpl
import com.company.watchlist.data.repositories.base.BaseRepository
import com.company.watchlist.data.repositories.base.BaseRepositoryImpl
import com.company.watchlist.data.repositories.main_activity.MainActivityRepository
import com.company.watchlist.data.repositories.main_activity.MainActivityRepositoryImpl
import com.company.watchlist.data.repositories.watchlist.WatchlistRepository
import com.company.watchlist.data.repositories.watchlist.WatchlistRepositoryImpl
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
    abstract fun bindBaseRepository(
        baseRepositoryImpl: BaseRepositoryImpl
    ): BaseRepository

    @Binds
    @Singleton
    abstract fun bindMainActivityRepository(
        mainActivityRepositoryImpl: MainActivityRepositoryImpl
    ): MainActivityRepository

    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindWatchlistRepository(
        watchlistRepositoryImpl: WatchlistRepositoryImpl
    ): WatchlistRepository

}