package com.example.loginpagetutorial.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = Modifier.statusBarsPadding().padding(top = 10.dp),
        title = { Text(text ="") },
        backgroundColor = Color.Transparent,
        elevation = 0.dp, // no shadow
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier.size(50.dp),
                        tint = Color.LightGray
                    )
                }
            }
        },
        actions = {
            if (showProfileIcon) {
                IconButton(onClick = { onProfileClick?.invoke() }, modifier = Modifier.padding(end = 20.dp)) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription =  "Profile",
                        modifier = Modifier.size(50.dp),
                        tint = Color.LightGray
                    )
                }
            }
        }
    )
}