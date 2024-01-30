package com.company.watchlist.ui.authentication.resetpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.watchlist.R
import com.company.watchlist.presentation.resetpassword.ResetPasswordEvent
import com.company.watchlist.presentation.resetpassword.ResetPasswordState
import com.company.watchlist.ui.components.ErrorAlertDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordState,
    resetPasswordChannelEvents: Flow<ResetPasswordEvent>,
    onEvent: (ResetPasswordEvent) -> Unit,
    goBack: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var showLinkSentDialog by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
    ) {

        if (state.loadingError != null) {
            ErrorAlertDialog(state.loadingError, {onEvent(ResetPasswordEvent.DismissError)}){
                onEvent(ResetPasswordEvent.Submit)
            }
        }


        LaunchedEffect(key1 = resetPasswordChannelEvents) {
            resetPasswordChannelEvents.collectLatest { event ->
                if (event == ResetPasswordEvent.ResetPasswordSuccessful) {
                    showLinkSentDialog = true
                }
            }
        }

        OutlinedTextField(
            value = state.email,
            label = { Text(text = "Email") },
            onValueChange = {
                onEvent(ResetPasswordEvent.EmailChanged(it))
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
                imeAction = ImeAction.Done
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
        if (state.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(40.dp))
        }
        Button(
            onClick = {
                onEvent(ResetPasswordEvent.Submit)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text(text = "Sent Link")
        }

        if (showLinkSentDialog) {
            AlertDialog(
                confirmButton = {
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp, 30.dp, 0.dp, 0.dp)
                    .align(Alignment.CenterHorizontally),
                text = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f),
                            painter = painterResource(id = R.drawable.inbox_cleanup_amico),
                            contentDescription = "Check Inbox Icon"
                        )
                        Text(
                            text = "Check your inbox",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Text(
                            text = "A link has just been sent to your email. Please check your inbox and use it to reset your password. Do not forget to check your spam folder",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 20.dp, 0.dp, 30.dp),
                        )
                        Button(
                            onClick = goBack,
                        ) {
                            Text(
                                text = "Return to login",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                },
                tonalElevation = 20.dp,
                onDismissRequest = goBack
            )
        }
    }
}