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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.data.local.entity.TaskStatus
import com.example.loginpagetutorial.components.TopBar

@Composable
fun TaskDetail(navController: NavController) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("")}
    var taskStatus by remember { mutableStateOf(TaskStatus.PENDING)} // TODO: this is hardcoded needs to receive task status from the task selected.

    Scaffold(
        topBar = { TopBar(navController = null, showBinIcon = true) }
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
                fontFamily = FontFamily.Monospace
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
            if(taskStatus != TaskStatus.DONE) {
                Spacer(Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        // TODO: Implement Cancel Task
                        navController.navigate("home")
                    }
                ) {
                    Text(
                        text = if(taskStatus != TaskStatus.CANCELLED) "Cancel Task" else "Uncancel Task",
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

            Button (
                onClick ={
                    // TODO: Implement Task Update Logic Here
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
        }
    }
}