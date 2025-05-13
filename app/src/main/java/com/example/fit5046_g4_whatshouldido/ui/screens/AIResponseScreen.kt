package com.example.fit5046_g4_whatshouldido.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun AIResponse(navController: NavController) {

    // dummy data
    val aiMessages = listOf(
        "Based on your tasks list, here's an optimal prioritization",
        "1. Drink water in the morning",
        "2. Study FIT5046 as assignment is due",
        "3. ......",
        "4. .....",
        "5. ...",
        "If you wish to follow the suggestion,\npress Yes, otherwise press No"
    )

    var isYesSelected by remember { mutableStateOf(false) }
    var isNoSelected by remember { mutableStateOf(false)}
    Scaffold(
        topBar = { TopBar(navController = navController, showProfileIcon = true) },
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
                fontFamily = FontFamily.Monospace
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
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF9F9F9))
                            .padding(16.dp)
                    ) {
                        Column {
                            aiMessages.forEach { message ->
                                Text(
                                    text = message,
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
                        navController.navigate("task_detail") {
                            popUpTo("ai_response") { inclusive = true }
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
                        // TODO: implement generating new suggestion
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