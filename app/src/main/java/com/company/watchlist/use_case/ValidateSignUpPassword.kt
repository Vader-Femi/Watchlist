package com.company.watchlist.use_case

import android.content.Context
import com.company.watchlist.R

class ValidateSignUpPassword(val context: Context) {

    /**
     * Input will be invalid if:
     * Password is less that 8,
     * Password doesn't contain a letter A-Z or a-z,
     * Password doesn't contain a digit 0-9,
     * Password doesn't contain a special character, or
     * Password contains a space,
     */
    fun execute(password: String): ValidationResult {

        if (password.length < 8) {
            return ValidationResult(
                false,
                context.getString(R.string.password_less_that_8_error)
            )
        }
        if (!password.any{it.isLetter()}) {
            return ValidationResult(
                false,
                context.getString(R.string.password_not_contain_letter_error)
            )
        }
        if (!password.any{it.isDigit()}) {
            return ValidationResult(
                false,
                context.getString(R.string.password_not_contain_number_error)
            )
        }
        if (!password.any{!it.isDigit() && !it.isLetter()} ) {
            return ValidationResult(
                false,
                context.getString(R.string.password_not_contain_special_char_error)
            )
        }
        if (password.contains(" ")) {
            return ValidationResult(
                false,
                context.getString(R.string.password_contain_space_error)
            )
        }
        return ValidationResult(
            true
        )
    }
}