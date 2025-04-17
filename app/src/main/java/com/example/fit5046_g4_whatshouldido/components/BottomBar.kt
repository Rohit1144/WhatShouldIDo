package com.example.loginpagetutorial.components

import androidx.compose.ui.graphics.vector.ImageVector
import Icon_Chart
import Icon_Home
import Icon_Sparkle
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

private val bottomNavItems = listOf(
    BottomNavItem("ask_ai", Icon_Sparkle, "Ask AI"),
    BottomNavItem("home", Icon_Home, "Home"),
    BottomNavItem("report", Icon_Chart,"Report")
)

@Composable
fun BottomNavBar(navController: NavController){
    BottomNavigation{
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = false,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = currentDestination?.route == item.route
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

        }
    }
}