package com.example.fit5046_g4_whatshouldido

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.fit5046_g4_whatshouldido.navigation.NavGraph
import com.example.fit5046_g4_whatshouldido.ui.theme.FIT5046_G4_WhatShouldIDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FIT5046_G4_WhatShouldIDoTheme {
//                Main()
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}

//@Composable
//fun Main(){
//    val navController = rememberNavController()
//    val navRoutes = listOf(
//        NavRoute("askai", FontAwesomeIcons.Solid.Microchip, "Ask AI"),
//        NavRoute("home", FontAwesomeIcons.Solid.Home, "Home"),
//        NavRoute("report", FontAwesomeIcons.Solid.ChartBar, "Report")
//
//    )
//
//    Scaffold(
//        bottomBar = {
//            BottomNavigationBar(navController = navController, navRoutes = navRoutes)
//        }
//    ){ paddingValues ->
//        NavGraph(navController = navController, modifier = Modifier.padding(paddingValues))
//    }
//}