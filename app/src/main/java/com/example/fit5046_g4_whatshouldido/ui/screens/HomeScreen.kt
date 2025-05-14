package com.example.fit5046_g4_whatshouldido.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.loginpagetutorial.components.BottomNavBar
import com.example.loginpagetutorial.components.TopBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.data.local.entity.TaskStatus
import okhttp3.internal.concurrent.Task

data class TaskItem (
    val title: String,
    val status: TaskStatus
)

@Composable
fun Home(
    navController: NavController
) {
    val taskList = remember {
        mutableStateListOf(
            TaskItem("Example 1 Task",TaskStatus.DONE),
            TaskItem("Example 2 Task",TaskStatus.DONE),
            TaskItem("Example 3 Task",TaskStatus.PENDING),
            TaskItem("Example 4 Task",TaskStatus.CANCELLED),
            TaskItem("Example 5 Task",TaskStatus.CANCELLED),
            TaskItem("Example 6 Task",TaskStatus.DONE),
            TaskItem("Example 7 Task",TaskStatus.DONE),
            TaskItem("Example 8 Task",TaskStatus.CANCELLED),
            TaskItem("Example 9 Task",TaskStatus.PENDING),
        )
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
                color = Color.DarkGray,
                fontFamily = FontFamily.Monospace
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
                            taskList[index] = task.copy(status = if(task.status != TaskStatus.DONE) TaskStatus.DONE else TaskStatus.PENDING)
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

@Composable
fun TaskItemRow(item: TaskItem, navController: NavController, onStatusToggle: () -> Unit) {

    Row(
       modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
       verticalAlignment = Alignment.CenterVertically
    ){
            // Status Toggle
            IconButton(
                onClick = onStatusToggle
            ) {
                Icon(
                    imageVector = if (item.status == TaskStatus.DONE) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle Status",
                    tint = if(item.status == TaskStatus.DONE) Color.Blue else Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = item.title,
                modifier = Modifier.weight(1f).padding(start = 4.dp).clickable { navController.navigate("task_detail") },
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if(item.status == TaskStatus.CANCELLED) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = if(item.status == TaskStatus.DONE) Color.LightGray else Color.DarkGray,
            )
    }
}