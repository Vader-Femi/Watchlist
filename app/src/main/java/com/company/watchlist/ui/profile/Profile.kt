package com.company.watchlist.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.watchlist.presentation.profile.ProfileEvent
import com.company.watchlist.presentation.profile.ProfileState
import com.company.watchlist.ui.components.ErrorAlertDialog
import com.company.watchlist.ui.components.MyPullRefreshIndicator


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
) {

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onEvent(ProfileEvent.LogOut)
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account") },
            text = { Text("This action cannot be undone. Are you sure?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onEvent(ProfileEvent.DeleteAccount)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("No")
                }
            }
        )
    }


    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onEvent(ProfileEvent.GetProfile) }
    )


    if (state.error != null) {
        ErrorAlertDialog(state.error, { onEvent(ProfileEvent.DismissError) }) {
            onEvent(ProfileEvent.GetProfile)
        }
    }

    LaunchedEffect(key1 = true) {
        onEvent(ProfileEvent.GetProfile)
    }

    LazyColumn(
        modifier = Modifier
            .padding(15.dp, 0.dp, 15.dp, 0.dp)
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                ),
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )

                    Text(
                        text = state.profile.email,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    if (state.isEditingProfile) {
                        OutlinedTextField(
                            value = state.profile.firstname,
                            onValueChange = { onEvent(ProfileEvent.FirstNameChanged(it)) },
                            label = { Text("First Name") },
                            maxLines = 2,
                            leadingIcon = {
                                Icon(Icons.Filled.Person, "First Name Icon")
                            },
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.None,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                        )
                        OutlinedTextField(
                            value = state.profile.lastname,
                            onValueChange = { onEvent(ProfileEvent.LastNameChanged(it)) },
                            label = { Text("Last Name") },
                            maxLines = 2,
                            leadingIcon = {
                                Icon(Icons.Filled.Person, "Last Name Icon")
                            },
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.None,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                        )

                    } else {
                        Text(
                            "First Name: ${state.profile.firstname}",
                            fontWeight = FontWeight.Medium
                        )
                        Text("Last Name: ${state.profile.lastname}", fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = {
                            if (state.isEditingProfile) {
                                onEvent(ProfileEvent.UpdateNames)
                            } else {
                                onEvent(ProfileEvent.IsEditingProfileChanged(true))
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (state.isEditingProfile) Icons.TwoTone.Done else Icons.TwoTone.Edit,
                            contentDescription = "Edit Profile Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (state.isEditingProfile) "Save" else "Edit",
                            fontSize = 17.sp,
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Delete",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = { showLogoutDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Logout",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(vertical = 3.dp))
                }
            }
        }

    }



    MyPullRefreshIndicator(
        isLoading = state.isLoading,
        pullRefreshState = pullRefreshState
    )

}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        state = ProfileState(),
        onEvent = { },
    )
}