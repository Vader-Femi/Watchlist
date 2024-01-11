package com.company.watchlist.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.company.watchlist.data.BottomNavBarData

@Composable
fun BottomNavigationBar(
    items: List<BottomNavBarData.BottomBarItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavBarData.BottomBarItem) -> Unit,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = modifier
            .padding(18.dp, 10.dp, 18.dp, 8.dp)
            .shadow(
                elevation = 15.dp,
                spotColor = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(10.dp)
            ),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    onItemClick(item)
                },
                label = {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.name} Icon"
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(

                )
            )

        }
    }
}