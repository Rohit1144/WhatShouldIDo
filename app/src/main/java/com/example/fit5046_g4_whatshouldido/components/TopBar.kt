package com.example.loginpagetutorial.components

import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TopBar(
    navController: NavController? = null,
    showBackButton: Boolean = false,
    showProfileIcon: Boolean = false,
    onBackClick: (() -> Unit)? = { navController?.popBackStack() },
    onProfileClick: (() -> Unit)? = { navController?.navigate("profile") }
) {
    TopAppBar(
        title = { Text(text ="", color = Color.LightGray) },
        backgroundColor = MaterialTheme.colorScheme.primary,
        elevation = 4.dp,
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.LightGray)
                }
            }
        },
        actions = {
            if (showProfileIcon) {
                IconButton(onClick = { onProfileClick?.invoke() }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.LightGray)
                }
            }
        }
    )
}