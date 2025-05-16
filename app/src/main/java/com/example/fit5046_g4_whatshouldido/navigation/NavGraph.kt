package com.example.fit5046_g4_whatshouldido.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fit5046_g4_whatshouldido.ui.screens.AIResponse
import com.example.fit5046_g4_whatshouldido.ui.screens.AddTask
import com.example.fit5046_g4_whatshouldido.ui.screens.AskAI
import com.example.fit5046_g4_whatshouldido.ui.screens.ChangePassword
import com.example.fit5046_g4_whatshouldido.ui.screens.Home
import com.example.fit5046_g4_whatshouldido.ui.screens.OnBoarding
import com.example.fit5046_g4_whatshouldido.ui.screens.Profile
import com.example.fit5046_g4_whatshouldido.ui.screens.Report
import com.example.fit5046_g4_whatshouldido.ui.screens.SavedQuotesScreen
import com.example.fit5046_g4_whatshouldido.ui.screens.SignIn
import com.example.fit5046_g4_whatshouldido.ui.screens.SignUp
import com.example.fit5046_g4_whatshouldido.ui.screens.SplashScreen
import com.example.fit5046_g4_whatshouldido.ui.screens.TaskDetail

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("sign_in"){ SignIn(navController) }
        composable("sign_up"){ SignUp(navController) }
        composable("on_boarding"){ OnBoarding(navController) }
        composable("home"){ Home(navController) }
        composable("add_task"){ AddTask(navController) }
        composable("task_detail/{taskId}"){
            backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            TaskDetail(navController, taskId.toString()) }
        composable("report"){ Report(navController) }
        composable("ask_ai"){ AskAI(navController) }
        composable("ai_response"){ AIResponse(navController) }
        composable("profile"){ Profile(navController) }
        composable("change_password"){ ChangePassword(navController) }
        composable("saved_quotes") { SavedQuotesScreen(navController) }
        composable("splash") { SplashScreen(navController) }
    }
}