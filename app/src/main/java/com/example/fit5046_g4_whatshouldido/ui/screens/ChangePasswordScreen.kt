package com.example.fit5046_g4_whatshouldido.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.components.TopBar
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun ChangePassword(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val user = Firebase.auth.currentUser
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var newConfirmPasswordVisible by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                showProfileIcon = false,
                showBackButton = false
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // heading
            Text(
                text = "Password Update",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )


            Spacer(modifier = Modifier.height(32.dp))


            // Current Password
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Current Password", color = colorResource(R.color.dark_gray)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Current Password")
                },
                singleLine = true,
                visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {

                    IconButton(
                        onClick = { currentPasswordVisible = !currentPasswordVisible }
                    ) {
                        Icon(
                            painter = painterResource(if (currentPasswordVisible) R.drawable.eye_slash else R.drawable.eye),
                            contentDescription = if (currentPasswordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // New Password
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password", color = colorResource(R.color.dark_gray)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "New Password")
                },
                singleLine = true,
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {

                    IconButton(
                        onClick = { newPasswordVisible = !newPasswordVisible }
                    ) {
                        Icon(
                            painter = painterResource(if (newPasswordVisible) R.drawable.eye_slash else R.drawable.eye),
                            contentDescription = if (newPasswordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm New Password", color = colorResource(R.color.dark_gray)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Confirm New Password"
                    )
                },
                singleLine = true,
                visualTransformation = if (newConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {

                    IconButton(
                        onClick = { newConfirmPasswordVisible = !newConfirmPasswordVisible }
                    ) {
                        Icon(
                            painter = painterResource(if (newConfirmPasswordVisible) R.drawable.eye_slash else R.drawable.eye),
                            contentDescription = if (newConfirmPasswordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Buttons: Update & Cancel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (user != null) {

                            if (newPassword != confirmPassword) {
                                Toast.makeText(
                                    context,
                                    "Passwords do not match",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (newPassword == currentPassword) {
                                Toast.makeText(
                                    context,
                                    "New password cannot be same as current password",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            // Retrieve user
                            val credential =
                                EmailAuthProvider.getCredential(user.email!!, currentPassword)
                            scope.launch {
                                user.reauthenticate(credential)
                                    .addOnCompleteListener { authResult ->
                                        if (authResult.isSuccessful) {
                                            // update password
                                            user.updatePassword(newPassword)
                                                .addOnCompleteListener { updateTask ->
                                                    if (updateTask.isSuccessful) {
                                                        Toast.makeText(
                                                            context,
                                                            "Password updated successfully",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        navController.navigate("profile") {
                                                            popUpTo("change_password") {
                                                                inclusive = true
                                                            }
                                                        }
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "ERROR: Failed to update password",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Current password is incorrect",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.light_red),
                        disabledContainerColor = colorResource(R.color.light_red)
                    )
                ) {
                    Text("Update")
                }
                Button(
                    onClick = {
                        // Navigate to home after clicking the sign in button
                        navController.navigate("profile") {
                            popUpTo("change_password") { inclusive = true }
                        }
                    },
                    border = BorderStroke(1.dp, color = colorResource(R.color.dark_gray)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.transparent),
                        disabledContainerColor = colorResource(R.color.transparent)
                    )
                ) {
                    Text("Cancel", color = colorResource(R.color.dark_gray))
                }
            }
        }
    }
}