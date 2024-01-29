package com.company.watchlist.data

import android.content.Context
import android.widget.Toast

const val FAVOURITES_MOVIES = "FAVOURITES_MOVIES"
const val FAVOURITES_SERIES = "FAVOURITES_SERIES"
fun Context.handleNetworkExceptions(
    exception: Exception? = null,
    message: String? = null
) {

    Toast.makeText(
        this,
        exception?.message ?: message,
        Toast.LENGTH_SHORT).show()

}