package com.company.watchlist.data.repositories.main_activity

interface MainActivityRepository {

    suspend fun isNewUser(): Boolean

}