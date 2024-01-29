package com.company.watchlist.use_case

import android.content.Context
import com.company.watchlist.R

class ValidateName(val context: Context) {

    fun execute(name: String): ValidationResult {
        if (name.isBlank()){
            return ValidationResult(
                false,
                context.getString(R.string.name_blank_error)
            )
        }
        return ValidationResult(
            true
        )
    }
}