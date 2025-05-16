package com.example.fit5046_g4_whatshouldido.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.fit5046_g4_whatshouldido.Managers.TaskManager
import com.example.fit5046_g4_whatshouldido.R
import com.example.loginpagetutorial.components.TopBar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

@Composable
fun AddTask(navController: NavController) {

    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val taskManager = remember { TaskManager() }

    Scaffold(
        topBar = { TopBar(navController = navController, showProfileIcon = false, showBackButton = true) },
    ) { paddingValues ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(40.dp).fillMaxSize().padding(paddingValues)
        ) {

            Text(
                text = "Add Task",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.DarkGray,
                fontFamily = FontFamily.Monospace
            )

            Spacer(Modifier.height(30.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title *") },
                placeholder = { Text("Write title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Task Description (multi-line)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Task Description") },
                placeholder = {
                    Text("Write description")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 8
            )

            Spacer(Modifier.height(120.dp))

            Button(
                onClick = {
//                    if(title.isNotEmpty() && description.isNotEmpty()){
                    if(title.isNotEmpty()){
                        scope.launch {
                            taskManager.addTask(title, description)
                        }
                        // Navigate to home after clicking the sign in button
                        navController.navigate("home") {
                            popUpTo("add_task") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "* Required fields have to be filled", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.light_red),
                    disabledContainerColor = colorResource(R.color.light_red)
                )
            ) {
                Text("OK")
            }
        }

    }
}