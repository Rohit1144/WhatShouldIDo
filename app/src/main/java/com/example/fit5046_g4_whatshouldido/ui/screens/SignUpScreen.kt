package com.example.fit5046_g4_whatshouldido.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fit5046_g4_whatshouldido.Managers.AuthResponse
import com.example.fit5046_g4_whatshouldido.Managers.AuthenticationManager
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.validations.FormValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavController) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var birthDate by remember { mutableStateOf("") }




    // DatePicker setup
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        birthDate = String.format("%02d/%02d/%04d", date.dayOfMonth, date.monthValue, date.year)
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val authenticationManager = remember { AuthenticationManager(context) }
    val formValidation = remember { FormValidation() }

    val emailRequester   = remember { FocusRequester() }
    val passRequester    = remember { FocusRequester() }
    val confirmRequester = remember { FocusRequester() }

    Column (
        modifier = Modifier.fillMaxSize().padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Sign Up",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            color = colorResource(R.color.dark_gray),
            fontFamily = FontFamily.Default
        )

        Spacer(Modifier.height(30.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name *") },
            placeholder = { Text("Your name, eg. Yasper") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.transparent),
                unfocusedContainerColor = colorResource(R.color.transparent),
                disabledContainerColor = colorResource(R.color.transparent),
                errorContainerColor = colorResource(R.color.transparent)
            )
        )

        Spacer(Modifier.height(30.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email *") },
            placeholder = { Text("eg. yasper.cordan@sample.com") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.transparent),
                unfocusedContainerColor = colorResource(R.color.transparent),
                disabledContainerColor = colorResource(R.color.transparent),
                errorContainerColor = colorResource(R.color.transparent)
            )
        )

        Spacer(Modifier.height(30.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password *") },
            placeholder = { Text("should contain at least 6 chars") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passRequester),
            visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
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

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password *") },
            placeholder = { Text("enter your password again") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(confirmRequester),
            visualTransformation =
                if (confirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = {confirmPasswordVisible = !confirmPasswordVisible}
                ) {
                    Icon(
                        painter = painterResource(if(confirmPasswordVisible) R.drawable.eye_slash else R.drawable.eye),
                        contentDescription = if(confirmPasswordVisible) "Hide password" else "Show password",
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

        TextField(
            value = birthDate,
            onValueChange = { }, // read-only
            label = { Text("Date of Birth *") },
            placeholder = { Text("DD/MM/YYYY") },
            singleLine = true,
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            trailingIcon = {
                IconButton (
                    onClick = { showDatePicker = true }
                )  {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.transparent),
                unfocusedContainerColor = colorResource(R.color.transparent),
                disabledContainerColor = colorResource(R.color.transparent),
                errorContainerColor = colorResource(R.color.transparent)
            )
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                val message = formValidation.validateSignUpForm(email, password, confirmPassword)
                if(message.isEmpty()) {
                    scope.launch {
                        val response = authenticationManager.createAccountWithEmail(email, password, name, birthDate)
                        when (response) {
                            is AuthResponse.Success -> {
                                delay(300) // gives Firebase some time to update auth state
                                withContext(Dispatchers.Main){
                                    navController.navigate("on_boarding") {
                                        popUpTo("sign_up") { inclusive = true }
                                    }
                                }
                            }
                            is AuthResponse.Error -> {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    scope.launch {
                        when (message) {
                            "Please enter a valid email" -> emailRequester.requestFocus()
                            "Password should be of length 6" -> passRequester.requestFocus()
                            "Password fields do not match" -> confirmRequester.requestFocus()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.light_red),
                disabledContainerColor = colorResource(R.color.light_red)
            ),
            enabled = name.isNotBlank()
                    && email.isNotBlank()
                    && password.isNotBlank()
                    && confirmPassword.isNotBlank()
                    && birthDate.isNotBlank()
        ) {
            Text("Sign Up")
        }

        Spacer(Modifier.height(20.dp))

        TextButton( onClick = {
            navController.navigate("sign_in")
        }) {
            Text(text = "Already have an account? Sign In",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.light_red),
                modifier = Modifier.drawBehind {
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