package com.example.fit5046_g4_whatshouldido.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.loginpagetutorial.components.TopBar

@Composable
fun ChangePassword(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController = navController, showProfileIcon = true) },
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            contentAlignment = Alignment.Center
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Password Management", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Password Screen. Let's Change your Password", style= MaterialTheme.typography.bodyLarge)
                Button (
                    onClick ={

                        // Navigate to home after clicking the sign in button
                        navController.navigate("home") {
                            popUpTo("change_password") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update")
                }
                Button (
                    onClick ={
                        // Navigate to home after clicking the sign in button
                        navController.navigate("profile") {
                            popUpTo("change_password") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}