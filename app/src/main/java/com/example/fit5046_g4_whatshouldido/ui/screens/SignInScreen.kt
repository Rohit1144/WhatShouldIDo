package com.example.fit5046_g4_whatshouldido.ui.screens



import android.widget.Toast
import com.example.fit5046_g4_whatshouldido.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.example.fit5046_g4_whatshouldido.managers.AuthResponse
import com.example.fit5046_g4_whatshouldido.managers.AuthenticationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SignIn(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val authenticationManager = remember { AuthenticationManager(context) }




    Column (
        modifier = Modifier.fillMaxSize().padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(R.drawable.app_logo_final),
            contentDescription = "App Logo",
            modifier = Modifier.size(170.dp)
        )

        Spacer(Modifier.height(5.dp))

        Column{
            Text(
                text = "Sign In",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                color = colorResource(R.color.dark_gray),
                fontFamily = FontFamily.Default
            )
        }

        Spacer(Modifier.height(30.dp))


        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.transparent),
                unfocusedContainerColor = colorResource(R.color.transparent),
                disabledContainerColor = colorResource(R.color.transparent),
                errorContainerColor = colorResource(R.color.transparent)
            ),

        )

        Spacer(Modifier.height(30.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {

                IconButton(
                    onClick = {passwordVisible = !passwordVisible}
                ) {
                    Icon(
                        painter = painterResource(if(passwordVisible) R.drawable.eye_slash else R.drawable.eye),
                        contentDescription = if(passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier.size(20.dp)
                    )
                }


            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.transparent),
                unfocusedContainerColor = colorResource(R.color.transparent),
                disabledContainerColor = colorResource(R.color.transparent),
                errorContainerColor = colorResource(R.color.transparent)
            )
        )

        Spacer(Modifier.height(30.dp))

        Button (
            onClick ={
                if(email.isNotEmpty() && password.isNotEmpty()){
                    scope.launch {
                        when (val response = authenticationManager.loginWithEmail(email, password)) {
                            is AuthResponse.Success -> {
                                if (response.isOnboarded) {
                                    // Navigate to home after clicking the sign in button
                                    navController.navigate("home") {
                                        popUpTo("sign_in") { inclusive = true }
                                    }
                                } else {
                                    navController.navigate("on_boarding") {
                                        popUpTo("sign_in") { inclusive = true }
                                    }
                                }
                            }
                            is AuthResponse.Error -> {
                                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Invalid email or password", Toast.LENGTH_LONG).show()
                }

            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.light_red),
                disabledContainerColor = colorResource(R.color.light_red)
            )
        ) {
            Text("Sign In")
        }

        Spacer(Modifier.height(24.dp))

        Text(text = "or use Google Sign-in", style = MaterialTheme.typography.bodySmall, color = colorResource(R.color.mid_gray), fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch(Dispatchers.Main) {
                    when (val response = authenticationManager.signInWithGoogle()) {
                        is AuthResponse.Success -> {
                            if (response.isOnboarded) {
                                navController.navigate("home") {
                                    popUpTo("sign_in") { inclusive = true }
                                }
                            } else {
                                navController.navigate("on_boarding") {
                                    popUpTo("sign_in") { inclusive = true }
                                }
                            }
                        }
                        is AuthResponse.Error -> {
                            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.light_gray),
                disabledContainerColor = colorResource(R.color.mid_gray)
            )
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google logo",
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified

            )

        }


        Spacer(Modifier.height(20.dp))


        TextButton( onClick = {
            navController.navigate("sign_up")
        }) {
            Text(text = "Don't have an account? Sign Up",
                style = MaterialTheme.typography.bodySmall,
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
}

