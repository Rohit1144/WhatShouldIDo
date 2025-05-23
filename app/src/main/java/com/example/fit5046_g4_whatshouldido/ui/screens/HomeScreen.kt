package com.example.fit5046_g4_whatshouldido.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.loginpagetutorial.components.BottomNavBar
import com.example.loginpagetutorial.components.TopBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.graphics.toColorInt
import com.example.fit5046_g4_whatshouldido.Managers.AuthenticationManager
import com.example.fit5046_g4_whatshouldido.Managers.TaskManager
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.data.local.entity.TaskStatus
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.concurrent.Task
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Home(
    navController: NavController
) {
    val taskList = remember { mutableStateListOf<Map<String, Any?>>() }
    val user = Firebase.auth.currentUser
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context) }
    val taskManager = remember { TaskManager() }
    var name by remember { mutableStateOf("") }

    LaunchedEffect("getName") {
        scope.launch {
            name = authenticationManager.getName()
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            val tasks = taskManager.getTaskList()
                .filter {
                    val isArchived = it["isArchived"] as? Boolean ?: false
                    !isArchived
                }

            taskList.clear()
            taskList.addAll(tasks)
        }
    }

    Scaffold(
        topBar = { TopBar(navController = navController, showProfileIcon = true) },
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header
            Text(
                text = "Home",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_gray),
                fontFamily = FontFamily.Default
            )
            Spacer(modifier = Modifier.height(29.dp))

            Text(
                text = "Welcome $name!",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_gray),
                fontFamily = FontFamily.Default
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Implement Lazy Column for infinite scroll
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth()
            ){
                itemsIndexed(taskList) { index, task ->
                    // TaskItem Composable
                    TaskItemRow(
                        task,
                        navController,
                        onStatusToggle = {
                            val updatedStatus = if (task["status"] != "DONE") "DONE" else "PENDING"
                            val taskId = task["id"] as String
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            val updatedAt = LocalDateTime.now().format(formatter)

                            scope.launch {
                                taskManager.updateTaskStatusToDone(updatedStatus, taskId, updatedAt) // Due to data discrepancy, needs to pass it as an argument

                                // Update local state for better UX
                                val updatedTask = task.toMutableMap().apply {
                                    this["status"] = updatedStatus
                                    this["updatedAt"] = updatedAt
                                    this["completedAt"] = if(updatedStatus == "DONE") updatedAt else null
                                }
                                taskList[index] = updatedTask
                            }
                        }
                    )
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Task Button
            Button (
                onClick ={
                    // Navigate to home after clicking the Add Task button
                    navController.navigate("add_task") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.light_red),
                    disabledContainerColor = colorResource(R.color.light_red)
                )
            ) {
                Text("Add Task")
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TaskItemRow(item: Map<String, Any?>, navController: NavController, onStatusToggle: () -> Unit) {

    val title = item["title"] as? String ?: ""
    val status = item["status"] as? String ?: "PENDING"
    val due = item["dueAt"] as? String ?: ""

    // Format time to show hh:mm:ss
    val timeRemaining = remember { mutableStateOf("") }

    LaunchedEffect(key1 = due) {
        while(true) {
            try{
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                val dueDateTime = LocalDateTime.parse(due, formatter)

                val now = LocalDateTime.now()
                val duration = java.time.Duration.between(now, dueDateTime)

                val seconds = duration.seconds
                if (seconds > 0) {
                    val days = seconds / (24 * 3600)
                    val hours = (seconds % (24 * 3600)) / 3600
                    val minutes = (seconds % 3600) / 60
                    val secs = seconds % 60

                    if (days > 0) {
                        timeRemaining.value = String.format("%d day%s %02d:%02d:%02d", days, if (days > 1) "s" else "", hours, minutes, secs)
                    } else {
                        timeRemaining.value = String.format("%02d:%02d:%02d", hours, minutes, secs)
                    }
                } else {
                    timeRemaining.value = "OVERDUE"
                }
                kotlinx.coroutines.delay(1000)
            } catch (e: Exception) {
                timeRemaining.value = "--:--:--"
                break
            }
        }
    }

    Row(
       modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
       verticalAlignment = Alignment.CenterVertically
    ){
            // Status Toggle
            IconButton(
                onClick = onStatusToggle,
                enabled = status != "CANCELED" && timeRemaining.value != "OVERDUE"
            ) {
                Icon(
                    imageVector = if (status == "DONE") Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle Status",
                    tint = if(status == "DONE") colorResource(R.color.blue) else colorResource(R.color.dark_gray),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f).padding(start = 4.dp).clickable { navController.navigate("task_detail/${item["id"]}") },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if(status == "CANCELED") TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if(status == "DONE") colorResource(R.color.light_gray) else colorResource(R.color.dark_gray),
                )

                if(due.isNotBlank() && status == "PENDING") {
                    Text(
                        text = timeRemaining.value,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (timeRemaining.value == "OVERDUE") colorResource(R.color.red) else colorResource(R.color.orange)
                    )
                }
            }
    }
}