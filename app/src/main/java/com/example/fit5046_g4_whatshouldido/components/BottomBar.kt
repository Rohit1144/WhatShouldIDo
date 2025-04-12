package com.example.fit5046_g4_whatshouldido.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination

data class NavRoute(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavigationBar(
    navController: NavController,
    navRoutes: List<NavRoute>
) {
    BottomNavigation (
        modifier = Modifier.padding(bottom = 20.dp),
        backgroundColor = Color(0xFFFFFFFF)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navRoutes.forEach{ navRoute ->
            BottomNavigationItem(
                icon = { Icon(navRoute.icon, contentDescription = navRoute.label) },
                label = { Text(navRoute.label) },
                modifier = Modifier.size(20.dp),
                selected = currentDestination?.route == navRoute.route,
                onClick = {
                    navController.navigate(navRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id){
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

        }
    }
}