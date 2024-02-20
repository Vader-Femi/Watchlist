package com.company.watchlist.data.repositories.authentication

interface AuthenticationRepository {

    suspend fun userFName(fName: String)

    suspend fun userLName(lName: String)

}