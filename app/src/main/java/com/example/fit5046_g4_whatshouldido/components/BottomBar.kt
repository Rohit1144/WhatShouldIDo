package com.example.loginpagetutorial.components

import com.example.fit5046_g4_whatshouldido.R
import androidx.compose.ui.graphics.vector.ImageVector
import Icon_Chart
import Icon_Home
import Icon_Sparkle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

private val bottomNavItems = listOf(
    BottomNavItem("ask_ai", Icon_Sparkle, "Ask AI"),
    BottomNavItem("home", Icon_Home, "Home"),
    BottomNavItem("report", Icon_Chart,"Report")
)

@Composable
fun BottomNavBar(navController: NavController){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = colorResource(R.color.manaual_gray),
        contentColor = colorResource(R.color.dark_gray),
        modifier = Modifier.height(65.dp)
    ){
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.route == item.route

            BottomNavigationItem(
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if(isSelected) colorResource(R.color.light_red) else colorResource(R.color.dark_gray),
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text( text = item.label, color = if(isSelected) colorResource(R.color.light_red) else colorResource(R.color.dark_gray))
                    }
                },
                selected = isSelected,
                onClick = {
                    if (currentDestination?.route != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )

        }
    }
}