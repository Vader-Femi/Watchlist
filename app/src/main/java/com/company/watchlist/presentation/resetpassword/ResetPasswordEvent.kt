package com.company.watchlist.presentation.resetpassword

sealed class ResetPasswordEvent{
    data class EmailChanged(val email: String): ResetPasswordEvent()

    object Submit: ResetPasswordEvent()

    object ResetPasswordSuccessful: ResetPasswordEvent()
}
