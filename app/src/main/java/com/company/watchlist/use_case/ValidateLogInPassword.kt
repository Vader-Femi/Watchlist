package com.company.watchlist.use_case

import android.content.Context
import com.company.watchlist.R

class ValidateLogInPassword(val context: Context) {

    fun execute(password: String): ValidationResult {

        if (password.isEmpty()) {
            return ValidationResult(
                false,
                context.getString(R.string.password_blank_error)
            )
        }
        return ValidationResult(
            true
        )
    }
}