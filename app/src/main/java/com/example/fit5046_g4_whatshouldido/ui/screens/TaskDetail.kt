package com.example.fit5046_g4_whatshouldido.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.Delete

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fit5046_g4_whatshouldido.Managers.TaskManager
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.data.local.entity.TaskStatus
import com.example.loginpagetutorial.components.TopBar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun TaskDetail(navController: NavController, taskId: String) {

    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("")}
    var taskStatus by remember { mutableStateOf("")}
    var originalTitle by remember { mutableStateOf("") }
    var originalDescription by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val taskManager = remember { TaskManager() }

    LaunchedEffect(taskId) {
        if (user != null) {
//            val doc = Firebase.firestore
//                .collection("Users")
//                .document(user.uid)
//                .collection("tasks")
//                .document(taskId)
//                .get()
//                .await()
//
//            title = doc.getString("title") ?: ""
//            description = doc.getString("description") ?: ""
//            taskStatus = doc.getString("status") ?: ""
            val result = taskManager.getTaskDetail(taskId)
            result?.let {
                title = it.title
                description = it.description
                taskStatus = it.status

                // save initial state
                originalTitle = it.title
                originalDescription = it.description
            }
        }
    }


    Scaffold(
        topBar = { TopBar(navController = navController, showBinIcon = true, taskId = taskId) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Text(
                text = "Task Detail",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.DarkGray,
                fontFamily = FontFamily.Default
            )
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
              value = title,
              onValueChange = { title = it },
              label = { Text("Task Title *") },
              placeholder = { Text("") }, // TODO: Replace this with actual title
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
                    Text("") // TODO: Replace this with actual description
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 8
            )
            if(taskStatus != "DONE") {
                Spacer(Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        val toggleCancel = if (taskStatus == "CANCELED") "PENDING" else "CANCELED"
                        // Cancel Task
                        scope.launch {
                            taskManager.updateTaskStatusToCancel(toggleCancel, taskId)
                        }

                        navController.navigate("home")
                    }
                ) {
                    Text(
                        text = if(taskStatus != "CANCELED") "Cancel Task" else "Uncancel Task",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.light_red),
                        modifier = Modifier.drawBehind{
                            val strokeWidthPx = 1.dp.toPx()
                            val verticalOffset = size.height - 2.sp.toPx()
                            drawLine(
                                color = Color.Red,
                                strokeWidth = strokeWidthPx,
                                start = Offset(0f, verticalOffset),
                                end = Offset(size.width, verticalOffset)
                            )
                        }
                    )
                }
            }



            Spacer(Modifier.height(50.dp))

            val hasChanged = title != originalTitle || description != originalDescription

                Button (
                    onClick ={
                        // update Task Detail
                        scope.launch {
                            taskManager.updateTaskDetails(title, description, taskId)
                        }
                        navController.navigate("home")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.light_red),
                        disabledContainerColor = colorResource(R.color.light_red)
                    )
                ) {
                    Text("OK")
                }

                Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    // Reset values and go back
                    title = originalTitle
                    description = originalDescription
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = hasChanged, // Enable only when fields have changed
                border = BorderStroke(1.dp, color = Color.DarkGray),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    contentColor = if (hasChanged) Color.DarkGray else Color.LightGray // Optional styling
                )
            ) {
                Text("Discard")
            }
        }
    }
}