package com.example.fit5046_g4_whatshouldido.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fit5046_g4_whatshouldido.Managers.AuthenticationManager
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.ui.components.AnimatedLoadingText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context) }

    LaunchedEffect(Unit) {
        delay(3000) // Optional delay to show splash
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val onboardingDone = authenticationManager.isOnboardingComplete()
            if(onboardingDone){
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("on_boarding") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        } else {
            navController.navigate("sign_in") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // Loading UI - LOGO
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(R.drawable.app_logo_final),
            contentDescription = "App Logo",
            modifier = Modifier.size(400.dp)
        )
        Spacer(Modifier.height(5.dp))
        Text("What Should I Do?", style= MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(150.dp))
        AnimatedLoadingText()
    }
}
