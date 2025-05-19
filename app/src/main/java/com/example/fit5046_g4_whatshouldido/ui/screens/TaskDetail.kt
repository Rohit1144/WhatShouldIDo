package com.example.fit5046_g4_whatshouldido.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fit5046_g4_whatshouldido.Managers.TaskManager
import com.example.fit5046_g4_whatshouldido.R
import com.example.loginpagetutorial.components.TopBar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetail(navController: NavController, taskId: String) {

    val user = Firebase.auth.currentUser
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("")}
    var taskStatus by remember { mutableStateOf("")}
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var dueDate by remember { mutableStateOf("") }
    var dueTime by remember { mutableStateOf("") }
    var dueDateTime by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }

    // DatePicker setup
    if (showDatePicker) {

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        dueDate = String.format("%02d/%02d/%04d", date.dayOfMonth, date.monthValue, date.year)

                        showTimePicker = true
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if(showTimePicker) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                dueTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                showTimePicker = false

                // ⏰ Combine the final datetime here
                dueDateTime = if (dueDate.isNotEmpty()) "$dueDate $dueTime" else ""
            },
            hour,
            minute,
            true
        ).show()
    }


    var originalTitle by remember { mutableStateOf("") }
    var originalDescription by remember { mutableStateOf("") }
    var originalDueDateTime by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val taskManager = remember { TaskManager() }

    LaunchedEffect(taskId) {
        if (user != null) {
            val result = taskManager.getTaskDetail(taskId)
            result?.let {
                title = it.title
                description = it.description
                taskStatus = it.status
                dueDateTime = it.dueAt

                // NEW: split dueDateTime into date + time
                val parts = it.dueAt.split(" ")
                if (parts.size == 2) {
                    dueDate = parts[0]
                    dueTime = parts[1]
                }

                // save initial state
                originalTitle = it.title
                originalDescription = it.description
                originalDueDateTime = it.dueAt
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
                readOnly = taskStatus == "DONE" || taskStatus == "CANCELED",
                enabled = taskStatus != "DONE" && taskStatus != "CANCELED",
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
                readOnly = taskStatus == "DONE" || taskStatus == "CANCELED",
                enabled = taskStatus != "DONE" && taskStatus != "CANCELED",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 8
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = dueDateTime,
                onValueChange = { }, // read-only
                label = { Text("Due Date *") },
                placeholder = { Text("DD/MM/YYYY HH:MM") },
                singleLine = true,
                readOnly = true,
                enabled = taskStatus != "DONE" && taskStatus != "CANCELED",
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton (
                        onClick = { showDatePicker = true },
                        enabled = taskStatus != "DONE" && taskStatus != "CANCELED"
                    )  {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
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

            val hasChanged = title != originalTitle || description != originalDescription || dueDateTime != originalDueDateTime

                Button (
                    onClick ={
                        // update Task Detail
                        scope.launch {
                            taskManager.updateTaskDetails(title, description, dueDateTime, taskId)
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