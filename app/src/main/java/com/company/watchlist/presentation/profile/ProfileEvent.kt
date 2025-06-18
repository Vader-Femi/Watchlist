package com.company.watchlist.presentation.profile


sealed class ProfileEvent {
    data class IsLoadingChanged(val isLoading: Boolean): ProfileEvent()
    data class FirstNameChanged(val firstName: String): ProfileEvent()
    data class LastNameChanged(val lastName: String): ProfileEvent()
    data class IsEditingProfileChanged(val isEditingProfile: Boolean): ProfileEvent()
    data object UpdateNames: ProfileEvent()
    data object GetProfile: ProfileEvent()
    data object LogOut: ProfileEvent()
    data object DeleteAccount: ProfileEvent()
    data object DismissError: ProfileEvent()
}