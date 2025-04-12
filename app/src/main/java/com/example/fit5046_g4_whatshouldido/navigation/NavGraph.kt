package com.example.fit5046_g4_whatshouldido.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fit5046_g4_whatshouldido.ui.screens.AskAI
import com.example.fit5046_g4_whatshouldido.ui.screens.Home
import com.example.fit5046_g4_whatshouldido.ui.screens.Report

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("askai"){ AskAI() }
        composable("home"){ Home() }
        composable("report"){ Report() }
    }
}