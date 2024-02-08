package com.company.watchlist.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val DEFAULT_PAGE_INDEX = 1
const val PAGE_SIZE = 20

enum class ListType{
    FAVOURITESMOVIES,
    FAVOURITESSERIES
}

object FilmConverter {

    @TypeConverter
    @JvmStatic
    fun dataToListOfFilm(fbData: MutableMap<String, Any>): List<Film> {

        val favouriteMovies = mutableListOf<Film>()
        for (data in fbData) {
            val gson = Gson()
            val type = object : TypeToken<Film>() {}.type

            favouriteMovies.add(gson.fromJson(data.value.toString(),type))
        }

        return favouriteMovies
    }

    @TypeConverter
    @JvmStatic
    fun filmToGson(film: Film): String {
        val gson = Gson()
        val type = object : TypeToken<Film>() {}.type
        return gson.toJson(film, type)
    }

}