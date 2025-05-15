package com.example.fit5046_g4_whatshouldido.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fit5046_g4_whatshouldido.Managers.TaskManager
import com.example.fit5046_g4_whatshouldido.Managers.TaskSummary
import com.example.loginpagetutorial.components.BottomNavBar
import com.example.loginpagetutorial.components.TopBar
import com.example.fit5046_g4_whatshouldido.ui.components.DonutChart
import com.example.fit5046_g4_whatshouldido.ui.components.MonthlyBarChart
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun Report(navController: NavController) {
    val taskManager = remember { TaskManager() }
    var taskSummary by remember { mutableStateOf<TaskSummary?>(null) }

    LaunchedEffect(Unit) {
        try {
            taskSummary = taskManager.getTaskSummary()
        } catch (e: Exception) {
            Log.e("ReportScreen", "Failed to load task summary", e)
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
                text = "Report Analysis",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Implement Lazy Column for infinite scroll
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth()

            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Task State",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Right
                            )
                            Divider(
                                color = Color.DarkGray,
                                thickness = 2.dp,
                                modifier = Modifier
                                    .width(100.dp)
                                    .padding(top = 4.dp)
                                    .align(Alignment.End)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    if(taskSummary == null) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            taskSummary?.let { summary ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    DonutChart(label = "Completed", percentage = summary.percentageOfCompleted() , colorHex = "#7FE1AD")
                                    Spacer(Modifier.height(4.dp))
                                    Text("Completed", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    DonutChart(label = "Pending", percentage = summary.percentageOfPending(), colorHex = "#F85F6A")
                                    Spacer(Modifier.height(4.dp))
                                    Text("Pending", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    DonutChart(label = "Cancelled", percentage = summary.percentageOfCancelled(), colorHex = "#5F6AF8")
                                    Spacer(Modifier.height(4.dp))
                                    Text("Cancelled", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                            }

                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Monthly Report",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Right
                            )
                            Divider(
                                color = Color.DarkGray,
                                thickness = 2.dp,
                                modifier = Modifier
                                    .width(100.dp)
                                    .padding(top = 4.dp)
                                    .align(Alignment.End)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF9F9F9))
                    ) {
                        // TODO: refactor this
                        MonthlyBarChart(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            barValues = listOf(3f,5f,4f,7f,2f,6f,4f,5f,8f,7f,4f,3f,6f)
                        )
                    }
                }
            }
        }
    }
}



