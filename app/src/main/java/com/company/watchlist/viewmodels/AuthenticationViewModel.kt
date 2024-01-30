package com.company.watchlist.viewmodels

import androidx.lifecycle.viewModelScope
import com.company.watchlist.data.repositories.authentication.AuthenticationRepositoryImpl
import com.company.watchlist.presentation.login.LogInEvent
import com.company.watchlist.presentation.login.LogInState
import com.company.watchlist.presentation.resetpassword.ResetPasswordEvent
import com.company.watchlist.presentation.resetpassword.ResetPasswordState
import com.company.watchlist.presentation.signup.SignUpEvent
import com.company.watchlist.presentation.signup.SignUpState
import com.company.watchlist.use_case.ValidateEmail
import com.company.watchlist.use_case.ValidateLogInPassword
import com.company.watchlist.use_case.ValidateName
import com.company.watchlist.use_case.ValidateSignUpPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthenticationRepositoryImpl,
    private val validateFirstName: ValidateName,
    private val validateLastName: ValidateName,
    private val validateEmail: ValidateEmail,
    private val validateSignUpPassword: ValidateSignUpPassword,
    private val validateLogInPassword: ValidateLogInPassword,
) : BaseViewModel(repository) {

    var signUpState = MutableStateFlow(SignUpState())
        private set

    private val signUpChannel = Channel<SignUpEvent>()
    val signUpChannelEvents = signUpChannel.receiveAsFlow()

    var logInState = MutableStateFlow(LogInState())
        private set

    private val logInChannel = Channel<LogInEvent>()
    val logInChannelEvents = logInChannel.receiveAsFlow()

    var resetPasswordState = MutableStateFlow(ResetPasswordState())
        private set

    private val resetPasswordChannel = Channel<ResetPasswordEvent>()
    val resetPasswordChannelEvents = resetPasswordChannel.receiveAsFlow()


    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.FirstNameChanged -> {
                signUpState.update {
                    it.copy(firstName = event.firstName, firstNameError = null)
                }
            }

            is SignUpEvent.LastNameChanged -> {
                signUpState.update {
                    it.copy(lastName = event.lastName, lastNameError = null)
                }
            }

            is SignUpEvent.EmailChanged -> {
                signUpState.update {
                    it.copy(email = event.email, emailError = null)
                }
            }

            is SignUpEvent.PasswordChanged -> {
                signUpState.update {
                    it.copy(password = event.password, passwordError = null)
                }
            }

            is SignUpEvent.Submit -> {
                signUp()
            }

            is SignUpEvent.ShowPasswordChanged -> {
                signUpState.update {
                    it.copy(showPassword = event.showPassword)
                }
            }

            is SignUpEvent.SignUpSuccessful -> {
                signUpState.update {
                    SignUpState()
                }
            }

            is SignUpEvent.DismissError -> {
                signUpState.update {
                    it.copy(loadingError = null)
                }
            }
        }
    }

    fun onEvent(event: LogInEvent) {
        when (event) {
            is LogInEvent.EmailChanged -> {
                logInState.update {
                    it.copy(email = event.email, emailError = null)
                }
            }

            is LogInEvent.PasswordChanged -> {
                logInState.update {
                    it.copy(password = event.password, passwordError = null)
                }
            }

            is LogInEvent.Submit -> {
                logIn()
            }

            is LogInEvent.ShowPasswordChanged -> {
                logInState.update {
                    it.copy(showPassword = event.showPassword)
                }
            }

            is LogInEvent.LogInSuccessful -> {
                logInState.update {
                    LogInState()
                }
            }

            is LogInEvent.DismissError -> {
                logInState.update {
                    it.copy(loadingError = null)
                }
            }
        }
    }

    fun onEvent(event: ResetPasswordEvent) {
        when (event) {
            is ResetPasswordEvent.EmailChanged -> {
                resetPasswordState.update {
                    it.copy(email = event.email, emailError = null)
                }
            }

            is ResetPasswordEvent.Submit -> {
                resetPassword()
            }

            is ResetPasswordEvent.ResetPasswordSuccessful -> {
                resetPasswordState.update {
                    ResetPasswordState()
                }
            }

            is ResetPasswordEvent.DismissError -> {
                resetPasswordState.update {
                    it.copy(loadingError = null)
                }
            }
        }
    }

    private fun signUp() {

        val email = signUpState.value.email
        val firstName = signUpState.value.firstName
        val lastName = signUpState.value.lastName
        val password = signUpState.value.password

        val emailResult = validateEmail.execute(email)
        val firstNameResult = validateFirstName.execute(firstName)
        val lastNameResult = validateLastName.execute(lastName)
        val passwordResult = validateSignUpPassword.execute(password)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            emailResult,
            passwordResult
        ).any { !it.successful }

        signUpState.update {
            it.copy(
                firstNameError = firstNameResult.errorMessage,
                lastNameError = lastNameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        }

        if (hasError)
            return


        signUpState.update {
            it.copy(isLoading = true)
        }

        repository.getAuthReference()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { addUserTask ->
                if (addUserTask.isSuccessful) {
                    viewModelScope.launch {
                        signUpState.update {
                            it.copy(isLoading = false, loadingError = null)
                        }
                        logIn()
                        signUpChannel.send(SignUpEvent.SignUpSuccessful)
                    }
                } else {
                    viewModelScope.launch {
                        signUpState.update {
                            it.copy(
                                isLoading = false,
                                loadingError = addUserTask.exception?.toString()
                                    ?: "Something went wrong 😪"
                            )
                        }
                    }
                }
            }
    }



    private fun logIn() {

        val email = logInState.value.email
        val password = logInState.value.password


        val emailResult = validateEmail.execute(email)
        val passwordResult = validateLogInPassword.execute(password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        logInState.update {
            it.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        }

        if (hasError)
            return

        logInState.update {
            it.copy(isLoading = true)
        }

        repository.getAuthReference()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { addUserTask ->
                if (addUserTask.isSuccessful) {
                    viewModelScope.launch {
                        logInState.update {
                            it.copy(isLoading = false, loadingError = null)
                        }
                        logInChannel.send(LogInEvent.LogInSuccessful)
                    }
                } else {
                    viewModelScope.launch {
                        logInState.update {
                            it.copy(
                                isLoading = false,
                                loadingError = addUserTask.exception?.toString()
                                    ?: "Something went wrong 😪"
                            )
                        }
                    }
                }
            }

    }


    private fun resetPassword() {
        val email = resetPasswordState.value.email

        val emailResult = validateEmail.execute(email)

        val hasError = listOf(
            emailResult
        ).any { !it.successful }

        resetPasswordState.update {
            it.copy(
                emailError = emailResult.errorMessage
            )
        }

        if (hasError)
            return

        resetPasswordState.update {
            it.copy(isLoading = true)
        }


        repository.getAuthReference()
            .sendPasswordResetEmail(email)
            .addOnCompleteListener { sendResetPasswordTask ->
                if (sendResetPasswordTask.isSuccessful) {
                    viewModelScope.launch {
                        resetPasswordChannel.send(ResetPasswordEvent.ResetPasswordSuccessful)
                    }
                    resetPasswordState.update {
                        it.copy(isLoading = false, loadingError = null)
                    }
                } else {
                    viewModelScope.launch {
                        resetPasswordState.update {
                            it.copy(
                                isLoading = false,
                                loadingError = sendResetPasswordTask.exception?.toString()
                                    ?: "Something went wrong 😪"
                            )
                        }
                    }
                }
            }
    }

}