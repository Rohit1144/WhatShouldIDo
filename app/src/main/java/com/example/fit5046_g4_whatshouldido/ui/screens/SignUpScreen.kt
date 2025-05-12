package com.example.fit5046_g4_whatshouldido.ui.screens

import android.app.DatePickerDialog
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
import com.example.fit5046_g4_whatshouldido.R
import java.util.Calendar

@Composable
fun SignUp(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var birthDate by remember { mutableStateOf("") }
//    var agreedToTerms by remember { mutableStateOf(false) }



    // DatePicker setup
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePicker = DatePickerDialog(
        context,
        { _, y, m, d ->
            birthDate = String.format("%02d/%02d/%04d", d, m + 1, y)
        },
        year, month, day
    )


    Column (
        modifier = Modifier.fillMaxSize().padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Sign Up",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.DarkGray,
            fontFamily = FontFamily.Monospace
        )

        Spacer(Modifier.height(30.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email *") },
            placeholder = { Text("Your email address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(30.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password *") },
            placeholder = { Text("Your password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
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
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(30.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password *") },
            placeholder = { Text("Your password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
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
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
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
                .clickable { datePicker.show() },
            trailingIcon = {
                IconButton (
                    onClick = { datePicker.show() }
                )  {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                // TODO: perform sign-up
                navController.navigate("home") {
                    popUpTo("sign_up") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.light_red),
                disabledContainerColor = colorResource(R.color.light_red)
            )
//            enabled = email.isNotBlank()
//                    && password.length >= 6
//                    && password == confirmPassword
//                    && birthDate.isNotBlank()
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