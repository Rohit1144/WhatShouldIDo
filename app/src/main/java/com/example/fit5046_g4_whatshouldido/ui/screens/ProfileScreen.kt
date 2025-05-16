package com.example.fit5046_g4_whatshouldido.ui.screens
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
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.viewmodel.ProfileViewModel
import com.example.loginpagetutorial.components.TopBar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable

@Composable
fun Profile(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {

//    var passwordVisible by remember { mutableStateOf(false) }
    val user = Firebase.auth.currentUser
    val email = user?.email ?: ""
    val isGoogleSignIn = user?.providerData?.any { it.providerId == "google.com" } == true
    val quote by viewModel.quote.collectAsState()
    val scope = rememberCoroutineScope()
    val isStarred = rememberSaveable { mutableStateOf(false) }

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
                color = Color.DarkGray,
                fontFamily = FontFamily.Monospace
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
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
            )

//            // Password Field
//            if(!isGoogleSignIn) {
//                Spacer(Modifier.height(30.dp))
//                OutlinedTextField(
//                    value = userPassword,
//                    onValueChange = {},
//                    label = { Text("Password") },
//                    readOnly = true,
//                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    trailingIcon = {
//
//                        IconButton(
//                            onClick = {passwordVisible = !passwordVisible}
//                        ) {
//                            Icon(
//                                painter = painterResource(if(passwordVisible) R.drawable.eye_slash else R.drawable.eye),
//                                contentDescription = if(passwordVisible) "Hide password" else "Show password",
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//
//
//                    },
//                    leadingIcon = {
//                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Password")
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        disabledContainerColor = Color.Transparent,
//                        errorContainerColor = Color.Transparent
//                    )
//                )
//            }

            // Retrofit API.
            Spacer(Modifier.height(30.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Todo: Put a spacer between the quote and author
                Text(
                    text = quote.let { "\"${it.q}\"\n– ${it.a}" },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .clickable { navController.navigate("saved_quotes") }
                )
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
                            Color(0xFFFFC107) else Color.Gray
                    )
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
        }
    }
}