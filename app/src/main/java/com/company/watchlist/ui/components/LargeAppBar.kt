package com.company.watchlist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.company.watchlist.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeAppBar(
    screen: Screen,
    backArrow: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {


    LargeTopAppBar(
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = screen.name,
                    style = TextStyle.Default.copy(
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                    )
                )
                screen.smallText?.let {
                    Text(
                        text = it,
                        style = TextStyle.Default.copy(
                            textAlign = TextAlign.Start,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            if (screen.name == Screen.MovieDetailsScreen.name || screen.name == Screen.SeriesDetailsScreen.name)
                IconButton(onClick = backArrow) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Arrow"
                    )
                }
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        scrollBehavior = scrollBehavior
    )
}