package com.example.fit5046_g4_whatshouldido.ui.screens

import androidx.compose.material.icons.filled.Delete

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TaskDetail(navController: NavController) {
    Scaffold(
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            contentAlignment = Alignment.Center
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Task Detail", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Task Detail Screen. You can Edit, Remove or press Cancel", style= MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Button (
                    onClick ={
                        // delete
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button (
                    onClick ={
                        // Navigate to home after clicking the sign in button
                        navController.navigate("home") {
                            popUpTo("task_detail") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK")
                }
                Button (
                    onClick ={
                        // Navigate to home after clicking the sign in button
                        navController.navigate("home") {
                            popUpTo("task_detail") { inclusive = true }
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