package com.company.watchlist.ui.authentication.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.watchlist.R
import com.company.watchlist.presentation.login.LogInEvent
import com.company.watchlist.presentation.login.LogInState
import com.company.watchlist.ui.components.ErrorAlertDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LogInScreen(
    state: LogInState,
    logInChannelEvents: Flow<LogInEvent>,
    onEvent: (LogInEvent) -> Unit,
    onLogIn: () -> Unit,
    goToResetPassword: () -> Unit,
    goToCreateAccount: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
    ) {

        if (state.loadingError != null) {
            ErrorAlertDialog(state.loadingError)
        }

        LaunchedEffect(key1 = logInChannelEvents) {
            logInChannelEvents.collectLatest { event ->
                if (event == LogInEvent.LogInSuccessful) {
                    onLogIn()
                }
            }
        }

        OutlinedTextField(
            value = state.email,
            label = { Text(text = "Email") },
            onValueChange = {
                onEvent(LogInEvent.EmailChanged(it))
            },
            isError = state.emailError != null,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            leadingIcon = {
                Icon(Icons.Filled.Email, "Email Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )

        )
        if (state.emailError != null) {
            Text(
                text = state.emailError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.password,
            label = { Text(text = "Password") },
            onValueChange = {
                onEvent(LogInEvent.PasswordChanged(it))
            },
            isError = state.passwordError != null,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            visualTransformation = if (state.showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                if (state.showPassword) {
                    IconButton(onClick = { onEvent(LogInEvent.ShowPasswordChanged(false)) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_visibility),
                            contentDescription = "Show Password"
                        )
                    }
                } else {
                    IconButton(onClick = { onEvent(LogInEvent.ShowPasswordChanged(true)) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_visibility_off),
                            contentDescription = "Hide Password"
                        )
                    }
                }
            },
            leadingIcon = {
                Icon(Icons.Filled.Lock, "Password Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Done
            )
        )
        if (state.passwordError != null) {
            Text(
                text = state.passwordError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        TextButton(
            onClick = goToResetPassword,
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Text(text = "Forgot Password?")
        }
        if (state.isLoading) {
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(20.dp))
        }
        Button(
            onClick = {
                onEvent(LogInEvent.Submit)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text(text = "Sign In")
        }
        Spacer(modifier = Modifier.height(40.dp))
        val annotatedText = buildAnnotatedString {
            pushStringAnnotation(
                tag = "Create Account Button",
                annotation = "Create Account"
            )

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                ),
            ) {
                append("Create Account? ")
            }

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                ),
            ) {
                append("Create Account")
            }

            pop()
        }
        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(
                    tag = "Create Account Button",
                    start = offset,
                    end = offset
                )
                    .firstOrNull()?.let {
                        goToCreateAccount()
                    }
            }

        )
    }
}