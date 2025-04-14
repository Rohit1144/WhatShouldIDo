package com.example.fit5046_g4_whatshouldido

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.fit5046_g4_whatshouldido.components.BottomNavigationBar
import com.example.fit5046_g4_whatshouldido.components.NavRoute
import com.example.fit5046_g4_whatshouldido.navigation.NavGraph
import com.example.fit5046_g4_whatshouldido.ui.theme.FIT5046_G4_WhatShouldIDoTheme
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ChartBar
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Microchip

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FIT5046_G4_WhatShouldIDoTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main(){
    val navController = rememberNavController()
    val navRoutes = listOf(
        NavRoute("askai", FontAwesomeIcons.Solid.Microchip, "Ask AI"),
        NavRoute("home", FontAwesomeIcons.Solid.Home, "Home"),
        NavRoute("report", FontAwesomeIcons.Solid.ChartBar, "Report")
        
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, navRoutes = navRoutes)
        }
    ){ paddingValues ->
        NavGraph(navController = navController, modifier = Modifier.padding(paddingValues))
    }
}