package com.company.watchlist.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.company.watchlist.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    screen: Screen,
    backArrow: (() -> Unit)?,
) {

    TopAppBar(
        title = {
            Text(
                text = screen.name,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        },
        navigationIcon = {
            if (backArrow != null)
                IconButton(onClick = backArrow) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Arrow",
                        tint = MaterialTheme.colorScheme.primary.copy(0.9f),
                    )
                }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            titleContentColor = MaterialTheme.colorScheme.onPrimary,
//            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
//            actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
        )
    )
}