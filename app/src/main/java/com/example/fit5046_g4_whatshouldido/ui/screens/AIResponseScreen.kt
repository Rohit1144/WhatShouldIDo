package com.example.fit5046_g4_whatshouldido.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loginpagetutorial.components.TopBar
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.RectangleShape
import com.example.fit5046_g4_whatshouldido.Managers.TaskManager
import dev.shreyaspatil.ai.client.generativeai.BuildConfig
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AIResponse(navController: NavController) {

    var isLoading by remember { mutableStateOf(true) }

    var responseText by remember { mutableStateOf("Waiting for response...") }
    val coroutineScope = rememberCoroutineScope()
    val taskManager = remember { TaskManager() }
    var taskList by remember { mutableStateOf(emptyList<Pair<String, String>>()) }
    var recommendedTaskId by remember { mutableStateOf<String?>(null) }


    var isYesSelected by remember { mutableStateOf(false) }
    var isNoSelected by remember { mutableStateOf(false)}

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                taskList = taskManager.getPendingTaskList()
                val words = taskList.map { it.second }

                responseText = if (words.isNotEmpty()) {
                    val result = generateTask(taskList)
                    recommendedTaskId = extractTaskId(result, taskList) // Extract the task ID
                    result
                } else {
                    "No pending tasks found."
                }
                isLoading = false
            } catch (e: Exception) {
                responseText = "Error fetching tasks: ${e.localizedMessage}"
                isLoading = false
            }
        }


    }
    Scaffold(
        topBar = { TopBar(navController = navController, showProfileIcon = true, showBackButton = true) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Text(
                text = "AI Response",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                fontFamily = FontFamily.Default
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Implement Lazy Column for infinite scroll
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF9F9F9))
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(15.dp))
                            .padding(16.dp)

                    ) {
                        Column (
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 20.dp)
                            ){
                                Text(
                                    text = responseText,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        isYesSelected = !isYesSelected
                        // Navigate to home after clicking the sign in button
                        recommendedTaskId?.let { taskId ->
                            navController.navigate("task_detail/$taskId") {
                                popUpTo("task_detail/$taskId") { inclusive = true }
                            }
                        }
                    },
                    border = BorderStroke(1.dp, color = if(isYesSelected) Color.Red else Color.DarkGray),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                ) {
                    Text("Yes",color = if(isYesSelected) Color.Red else Color.DarkGray)
                }
                Button(
                    onClick = {
                        isNoSelected = !isNoSelected
                        Toast.makeText(
                            navController.context,
                            "Oh, generating a new answer...",
                            Toast.LENGTH_SHORT
                        ).show()
                        coroutineScope.launch(Dispatchers.IO) {
                            //val result = generateTask(taskList, isDifferent = true)
                            //responseText = result
                            // Extract the new recommended task ID
                            //val newTaskId = extractTaskId(result, taskList)
                            // Update the state correctly
                            //if (newTaskId != null && newTaskId != "Unknown") {
                                //recommendedTaskId = newTaskId
                                //println("Updated Task ID: $recommendedTaskId")
                            //} else {
                                //recommendedTaskId = "Unknown"
                                //println("Failed to update Task ID")
                            //}
                            responseText =
                                generateDifferentResponse(taskList)
                        }
                    },
                    border = BorderStroke(1.dp, color = if(isNoSelected) Color.Red else Color.DarkGray),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                ) {
                    Text("No",color = if(isNoSelected) Color.Red else Color.DarkGray)

                }
            }
        }
    }
}

suspend fun generateTask(tasks: List<Pair<String, String>>): String {
    return try {
        val apiKey = com.example.fit5046_g4_whatshouldido.BuildConfig.GEMINI_API_KEY

        val generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = apiKey
        )
        val formattedTasks = tasks.joinToString(", ") { it.second }


        val inputContent = content {
            text("From the following list: $formattedTasks, select only one task to start with. " +
                    "Do not create or invent a new task. Only choose from the given list. " +
                    "Provide a brief explanation in two sentences and end by asking if the user is satisfied.")
        }


        val response = generativeModel.generateContent(inputContent)
        val cleanedResponse = response.text?.replace(Regex("\\(.*?\\)"), "")?.trim()

        val taskId = extractTaskId(cleanedResponse ?: "No response from AI.", tasks) ?: "Unknown"

        println("Generated Task ID: $taskId")

        "Recommended Task ID: $taskId\n$cleanedResponse"
    } catch (e: Exception) {
        "Error: ${e.localizedMessage}"
    }
}
fun extractTaskId(responseText: String, tasks: List<Pair<String, String>>): String? {
    for ((id, task) in tasks) {
        if (responseText.contains(task, ignoreCase = true) || responseText.contains(task.split(" ").first(), ignoreCase = true)) {
            return id
        }
    }
    return "Unknown"
}


suspend fun generateDifferentResponse(tasks: List<Pair<String, String>>): String {
    return try {
        val apiKey = com.example.fit5046_g4_whatshouldido.BuildConfig.GEMINI_API_KEY
        val generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = apiKey
        )
        val formattedTasks = tasks.joinToString(", ") { it.second }

        val inputContent = content {
            text(
                "From the following list: $formattedTasks, select only one randomly different task to start with. " +
                        "Do not create or invent a new task. Only choose from the given list. " +
                        "Provide a brief explanation in two sentences and end by asking if the user is satisfied.")
        }

        val response = generativeModel.generateContent(inputContent)
        val cleanedResponse = response.text?.replace(Regex("\\(.*?\\)"), "")?.trim()

        val taskId = extractTaskId(cleanedResponse ?: "No response from AI.", tasks) ?: "Unknown"

        println("Generated Task ID: $taskId")

        "Recommended Task ID: $taskId\n$cleanedResponse"
    } catch (e: Exception) {
        "Error: ${e.localizedMessage}"
    }
}
