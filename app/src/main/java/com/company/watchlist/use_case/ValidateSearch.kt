package com.company.watchlist.use_case

import android.content.Context
import com.company.watchlist.R

class ValidateSearch(private val context: Context) {

    fun execute(query: String): ValidationResult {

        if (query.isBlank()){
            return ValidationResult(
                false,
                context.getString(R.string.search_query_empty_error)
            )
        }

        return ValidationResult(
            true
        )
    }
}