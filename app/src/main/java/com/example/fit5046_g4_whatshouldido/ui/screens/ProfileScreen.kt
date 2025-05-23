package com.example.fit5046_g4_whatshouldido.ui.screens
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.viewmodel.QuoteViewModel
import com.example.loginpagetutorial.components.TopBar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.fit5046_g4_whatshouldido.Managers.TaskManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.fit5046_g4_whatshouldido.Managers.AuthResponse
import com.example.fit5046_g4_whatshouldido.Managers.AuthenticationManager
import com.example.fit5046_g4_whatshouldido.ui.components.DeleteConfirmationBottomSheet
import com.example.fit5046_g4_whatshouldido.ui.components.DeleteType

@Composable
fun Profile(
    navController: NavController,
    viewModel: QuoteViewModel = viewModel()
) {

    val user = Firebase.auth.currentUser
    val email = user?.email ?: ""
    val isGoogleSignIn = user?.providerData?.any { it.providerId == "google.com" } == true
    val quote by viewModel.quote.collectAsState()
    val scope = rememberCoroutineScope()
    val isStarred = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val taskManager = remember { TaskManager() }
    val authenticationManager = remember { AuthenticationManager(context) }
    var openResetSheet by remember { mutableStateOf(false) }
    var openAccountDeleteSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar(navController = navController, showProfileIcon = false, showBackButton = true) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            // Profile Icon
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_gray),
                fontFamily = FontFamily.Default
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Email Field (disabled Text Field)
            OutlinedTextField(
                value = email,
                onValueChange = {},
                readOnly = true,
                label = { Text("Email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.transparent),
                    unfocusedContainerColor = colorResource(R.color.transparent),
                    disabledContainerColor = colorResource(R.color.transparent),
                    errorContainerColor = colorResource(R.color.transparent)
                )
            )

            Spacer(Modifier.height(30.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .clickable { navController.navigate("saved_quotes") }
                ) {
                    Text(
                        text = if (quote.a != "") "\"${quote.q}\"\n\n– ${quote.a}" else "\"${quote.q}\"\n",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.dark_gray),
                        textAlign = TextAlign.Start
                    )
                    Spacer(Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = colorResource(R.color.gray),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Tap to view saved quotes",
                            style = MaterialTheme.typography.labelSmall,
                            color = colorResource(R.color.gray),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                if(quote.a != "") {
                    IconButton(
                        onClick = {
                            isStarred.value = !isStarred.value
                            scope.launch {
                                if (isStarred.value) {
                                    viewModel.saveQuote(quote)
                                } else {
                                    viewModel.deleteQuote(quote)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isStarred.value)
                                Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if (isStarred.value)
                                colorResource(R.color.mustard_yellow) else colorResource(R.color.gray)
                        )
                    }
                }
                if(quote.q == "Could not load quote. Try again later.")
                {
                    IconButton(onClick = {
                        scope.launch {
                            viewModel.fetchQuote()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh quote",
                            tint = colorResource(R.color.gray)
                        )
                    }
                }

            }
            Spacer(Modifier.height(30.dp))

            // Update Profile Button based on the method of login
            if(!isGoogleSignIn) {
                Button (
                    onClick ={
                        // Navigate to home after clicking the sign in button
                        navController.navigate("change_password") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.light_red),
                        disabledContainerColor = colorResource(R.color.light_red)
                    )
                ) {
                    Text("Update Password")
                }
            } else {
                Text(
                    text = "Logged in via Google",
                    color = colorResource(R.color.light_red),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button (
                onClick ={
                    Firebase.auth.signOut()
                    // Navigate to home after clicking the logout button
                    navController.navigate("sign_in") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.light_red),
                    disabledContainerColor = colorResource(R.color.light_red)
                )
            ) {
                Text("Log Out")
            }

            Spacer(Modifier.height(20.dp))

            TextButton( onClick = {
                openResetSheet = true
            }) {
                Text(text = "Factory Reset",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.light_red),
                    modifier = Modifier.drawBehind() {
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
            Spacer(Modifier.height(2.dp))

            TextButton( onClick = {
                openAccountDeleteSheet = true
            }) {
                Text(text = "Delete Account",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.light_red),
                    modifier = Modifier.drawBehind() {
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
    }
    if(openResetSheet) {
        DeleteConfirmationBottomSheet(
            onConfirmDelete = {
                scope.launch {
                    try {
                        // Delete all the tasks
                        taskManager.deleteAllTasks()
                        // Delete all the saved quotes
                        viewModel.deleteAllQuotes()

                        Toast.makeText(context, "All your tasks has been deleted. Let's start fresh", Toast.LENGTH_LONG).show()
                        kotlinx.coroutines.delay(1500)

                        navController.navigate("home") { popUpTo("profile") { inclusive = true } }
                    } catch ( e:Exception ){
                        Toast.makeText(context, e.message ?: "Reset Failed", Toast.LENGTH_LONG).show()
                    }
                }
            },
            onDismiss = { openResetSheet = false },
            DeleteType.FACTORY_RESET
        )
    }

    if(openAccountDeleteSheet) {

        DeleteConfirmationBottomSheet(
            onConfirmDelete = { enteredPassword ->
                scope.launch {
                    val isGoogleSignIn = user?.providerData?.any { it.providerId == "google.com" } == true

                    if ( !isGoogleSignIn && enteredPassword.isNullOrBlank()) {
                        Toast.makeText(context, "Please enter your password to confirm", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    try {
                        val response = authenticationManager.deleteAccount(
                            deleteTasks = { taskManager.deleteAllTasks() },
                            deleteQuotes = { viewModel.deleteAllQuotes() },
                            email = user?.email,
                            password = enteredPassword
                        )

                        when (response) {
                            is AuthResponse.Success -> {
                                navController.navigate("sign_in") {
                                    popUpTo(0) { inclusive = true }
                                }
                                Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_LONG).show()
                            }
                            is AuthResponse.Error -> {
                                Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_LONG).show()
                            }
                        }

                    } catch( e: Exception ) {
                        Toast.makeText(context, e.message ?: "Account Delete Failed", Toast.LENGTH_LONG).show()
                    }
                }
            },
            onDismiss = { openAccountDeleteSheet = false },
            DeleteType.ACCOUNT,
            showPasswordField = true
        )
    }
}

