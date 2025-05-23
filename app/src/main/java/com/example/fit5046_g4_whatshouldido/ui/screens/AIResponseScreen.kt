package com.example.fit5046_g4_whatshouldido.ui.screens

import android.app.Application
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fit5046_g4_whatshouldido.LocalLLMModel.GemmaLocalInference
import com.example.fit5046_g4_whatshouldido.managers.TaskManager
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.viewmodel.AiViewModel
import com.example.fit5046_g4_whatshouldido.components.TopBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AIResponse(navController: NavController) {
    val context = LocalContext.current
    val viewModel: AiViewModel = remember { AiViewModel(context.applicationContext as Application) }
    val isInitialized by viewModel.isInitialized

    val scope = rememberCoroutineScope()
    val taskManager = remember { TaskManager() }

    var isLoading by remember { mutableStateOf(true) }
    var responseText by remember { mutableStateOf("Waiting for response...") }
    var taskList by remember { mutableStateOf(emptyList<Pair<String, String>>()) }
    var recommendedTaskId by remember { mutableStateOf<String?>(null) }
    var lastSuggestedTaskTitle by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isInitialized) {
        if (isInitialized) {
            scope.launch(Dispatchers.IO) {
                try {
                    taskList = taskManager.getPendingTaskList()
                    val words = taskList.map { it.second }

                    responseText = if (words.isNotEmpty()) {
                        val result = generateTask(taskList)
                        recommendedTaskId = extractTaskId(result, taskList)
                        result
                    } else {
                        "No pending tasks found"
                    }
                } catch (e: Exception) {
                    responseText = "Error fetching tasks: ${e.localizedMessage}"
                } finally {
                    isLoading = false
                }
            }
        } else {
            responseText = "Initializing model ..."
        }
    }

    Scaffold(
        topBar = { TopBar(navController = navController, showBackButton = true) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "AI Response",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_gray),
                fontFamily = FontFamily.Default
            )

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorResource(R.color.background_gray))
                            .border(
                                1.dp,
                                colorResource(R.color.dark_gray),
                                RoundedCornerShape(15.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 20.dp)
                            ) {
                                Text(
                                    text = responseText,
                                    fontSize = 16.sp,
                                    color = colorResource(R.color.black),
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
                        recommendedTaskId?.let { taskId ->
                            navController.navigate("task_detail/$taskId") {
                                popUpTo("task_detail/$taskId") { inclusive = true }
                            }
                        }
                    },
                    border = BorderStroke(1.dp, colorResource(R.color.dark_gray)),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.transparent))
                ) {
                    Text("Yes", color = colorResource(R.color.dark_gray))
                }

                Button(
                    onClick = {
                        Toast.makeText(
                            navController.context,
                            "Oh, generating a new answer...",
                            Toast.LENGTH_SHORT
                        ).show()
                        scope.launch(Dispatchers.IO) {
                            try {
                                val result =
                                    generateDifferentResponse(taskList, lastSuggestedTaskTitle)
                                responseText = result
                                lastSuggestedTaskTitle = extractMatchedTaskTitle(result, taskList)

                                val newTaskId =
                                    extractTaskId(lastSuggestedTaskTitle.toString(), taskList)
                                recommendedTaskId =
                                    if (newTaskId != null && newTaskId != "Unknown") {
                                        newTaskId
                                    } else {
                                        "Unknown"
                                    }

                            } catch (e: Exception) {
                                responseText = "Error generating new task: ${e.localizedMessage}"
                            }
                        }
                    },
                    border = BorderStroke(1.dp, colorResource(R.color.dark_gray)),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.transparent))
                ) {
                    Text("No", color = colorResource(R.color.dark_gray))
                }
            }
        }
    }
}

fun extractMatchedTaskTitle(response: String, tasks: List<Pair<String, String>>): String? {
    return tasks.firstOrNull { (_, title) ->
        response.contains(title, ignoreCase = true)
    }?.second
}

fun generateTask(tasks: List<Pair<String, String>>): String {
    val formattedTasks = tasks.joinToString(", ") { it.second }
    val prompt =
        "From the following list: $formattedTasks, select only one  task and print it. " +
                "Do not create or invent a new task." +
                "Provide a brief explanation in two sentences and end by asking if the user is satisfied."


    val response = GemmaLocalInference.generate(prompt)
    val cleanedResponse = response.replace(Regex("\\(.*?\\)"), "").trim()

    val taskId = extractTaskId(response, tasks) ?: "Unknown"

    "Recommended Task ID: $taskId\n$cleanedResponse"
    return cleanedResponse

}

fun extractTaskId(responseText: String, tasks: List<Pair<String, String>>): String? {
    for ((id, task) in tasks) {
        if (responseText.contains(task, ignoreCase = true) || responseText.contains(
                task.split(" ").first(), ignoreCase = true
            )
        ) {
            return id
        }
    }
    return "Unknown"
}


fun generateDifferentResponse(tasks: List<Pair<String, String>>, lastTask: String?): String {
    val formattedTasks = tasks.joinToString(", ") { it.second }

    val prompt = """
        From the following list of tasks: $formattedTasks

        Pick ONE task that is DIFFERENT from "${lastTask ?: ""}".
        - First, print the task name.
        - Then explain in 1–2 sentences why it is a good choice.
        - Do NOT invent new tasks.
        - Only choose tasks from the list.
        - Keep it concise and clear.

        Response:
    """.trimIndent()
    val response = GemmaLocalInference.generate(prompt)

    val cleanedResponse = response.replace(Regex("\\(.*?\\)"), "").trim()

    return cleanedResponse
}


