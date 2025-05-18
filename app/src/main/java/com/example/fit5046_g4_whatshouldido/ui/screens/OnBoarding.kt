package com.example.fit5046_g4_whatshouldido.ui.screens

import com.example.fit5046_g4_whatshouldido.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.fit5046_g4_whatshouldido.Managers.AuthenticationManager
import com.example.fit5046_g4_whatshouldido.Managers.TaskManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun OnBoarding(navController: NavController) {

    val professions = listOf("Select","Student", "Professional", "Freelancer")
    val focusTimes = listOf("Select","Morning", "Afternoon", "Evening")
    val startPreferences = listOf("Select","Simpler Task", "Complex Task")

    var profession by remember { mutableStateOf(professions[0]) }
    var professionExpanded by remember { mutableStateOf(false) }

    var focusTime by remember { mutableStateOf(focusTimes[0]) }
    var focusExpanded by remember { mutableStateOf(false) }

    var startPreference by remember { mutableStateOf(startPreferences[0]) }
    var startExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context) }
    val taskManager = remember { TaskManager() }

    Column (
        modifier = Modifier.fillMaxSize().padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Onboarding",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.DarkGray,
            fontFamily = FontFamily.Monospace
        )

        Spacer(Modifier.height(30.dp))


        Box(
            modifier = Modifier.padding(16.dp)
        ) {



            TextField(
                readOnly = true,
                value = profession,
                onValueChange = { },
                label = { Text("What's Your Profession? *") },
                trailingIcon = {
                    IconButton(onClick = { professionExpanded = !professionExpanded }) {
                        Icon(
                            painter = painterResource(if(professionExpanded) R.drawable.up_arrow else R.drawable.down_arrow),
                            contentDescription = "Options",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodySmall
            )
            DropdownMenu(
                expanded = professionExpanded,
                onDismissRequest = { professionExpanded = false },
                modifier = Modifier.width(298.dp)
            ) {
                professions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            profession = option
                            professionExpanded = false
                        },
                        text = { Text(option) },

                    )
                }
            }
        }

        Spacer(Modifier.height(15.dp))

        Box(
            modifier = Modifier.padding(16.dp)
        ) {

            TextField(
                readOnly = true,
                value = focusTime,
                onValueChange = { },
                label = { Text("When do you focus the most? *") },
                trailingIcon = {
                    IconButton(onClick = { focusExpanded = !focusExpanded }) {
                        Icon(
                            painter = painterResource(if(focusExpanded) R.drawable.up_arrow else R.drawable.down_arrow),
                            contentDescription = "Options",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodySmall
            )
            DropdownMenu(
                expanded = focusExpanded,
                onDismissRequest = { focusExpanded = false },
                modifier = Modifier.width(298.dp)
            ) {
                focusTimes.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            focusTime = option
                            focusExpanded = false
                        },
                        text = { Text(option) },
                    )
                }
            }
        }

        Spacer(Modifier.height(15.dp))

        Box(
            modifier = Modifier.padding(16.dp)
        ) {

            TextField(
                readOnly = true,
                value = startPreference,
                onValueChange = { },
                label = { Text("Do you prefer to start with? *") },
                trailingIcon = {
                    IconButton(onClick = { startExpanded = !startExpanded }) {
                        Icon(
                            painter = painterResource(if(startExpanded) R.drawable.up_arrow else R.drawable.down_arrow),
                            contentDescription = "Options",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodySmall
            )
            DropdownMenu(
                expanded = startExpanded,
                onDismissRequest = { startExpanded = false },
                modifier = Modifier.width(298.dp)
            ) {
                startPreferences.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            startPreference = option
                            startExpanded = false
                        },
                        text = { Text(option) },
                    )
                }
            }
        }

        Spacer(Modifier.height(17.dp))


        Button(
            onClick ={
                scope.launch(Dispatchers.Main) {
                    authenticationManager.markOnboardingComplete(profession, focusTime, startPreference)
//                    taskManager.createExampleTasks(navController, profession)
                    taskManager.createExampleTasks(profession)
                    delay(300)
                    // Navigate to home after clicking the OK button
                    navController.navigate("home") {
                        popUpTo("on_boarding") { inclusive = true }
                    }
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