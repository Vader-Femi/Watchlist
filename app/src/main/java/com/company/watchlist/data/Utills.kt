package com.company.watchlist.data

import android.content.Context
import android.widget.Toast

enum class ListType{
    FAVOURITESMOVIES,
    FAVOURITESSERIES
}

enum class ListFields{
    ID,
    NAME,
    POSTERPATH,
    AVERAGERATING
}

fun Context.handleNetworkExceptions(
    exception: Exception? = null,
    message: String? = null
) {

    Toast.makeText(
        this,
        exception?.message ?: message,
        Toast.LENGTH_SHORT).show()

}