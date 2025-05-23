package com.example.loginpagetutorial.components

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fit5046_g4_whatshouldido.Managers.TaskManager
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.ui.components.DeleteConfirmationBottomSheet
import com.example.fit5046_g4_whatshouldido.ui.components.DeleteType
import kotlinx.coroutines.launch

@Composable
fun TopBar(
    navController: NavController? = null,
    showBackButton: Boolean = false,
    showProfileIcon: Boolean = false,
    showBinIcon: Boolean = false,
    taskId: String? = null,
    onBackClick: (() -> Unit)? = { navController?.navigate("home") {
        popUpTo("add_task") { inclusive = true }
    } },
    onProfileClick: (() -> Unit)? = { navController?.navigate("profile") }
) {
    var isBackSelected by remember { mutableStateOf(false) }
    var isProfileSelected by remember { mutableStateOf(false) }
    var isBinSelected by remember { mutableStateOf(false) }
    var openDeleteSheet by remember { mutableStateOf(false) } // To open bottom sheet for removing tasks

    TopAppBar(
        modifier = Modifier.statusBarsPadding().padding(top = 10.dp),
        title = { Text(text ="") },
        backgroundColor = colorResource(R.color.transparent),
        elevation = 0.dp, // no shadow
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    isBackSelected = !isBackSelected
                    onBackClick?.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier.size(35.dp),
                        tint = if(isBackSelected) colorResource(R.color.light_red) else colorResource(R.color.light_gray),
                    )
                }
            }
        },
        actions = {
            if (showProfileIcon) {
                IconButton(onClick = {
                    isProfileSelected = !isProfileSelected
                    onProfileClick?.invoke()
                    },
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription =  "Profile",
                        modifier = Modifier.size(50.dp),
                        tint = if(isProfileSelected) colorResource(R.color.light_red) else colorResource(R.color.light_gray),
                    )
                }
            } else if (showBinIcon) {
                IconButton(onClick = {
                    isBinSelected = !isBinSelected
                    // Opens deletion bottom sheet to ask if the user wants to really delete it or not
                    openDeleteSheet = true
                    },
                    modifier = Modifier.padding(end = 20.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        modifier = Modifier.size(30.dp),
                        tint = colorResource(R.color.light_red),
                    )
                }
            }
        }
    )
    // Opens up bottom sheet for deletion asking user again to delete the task or not
    if(openDeleteSheet) {
        val coroutineScope = rememberCoroutineScope()
        val taskManager = remember { TaskManager() }
        val context = LocalContext.current
        DeleteConfirmationBottomSheet(
            onConfirmDelete = {
                try{
                    if(taskId != null) {
                        coroutineScope.launch {
                            taskManager.archiveTask(taskId)
                            kotlinx.coroutines.delay(3000)
                        }
                    }

                    openDeleteSheet = false

                    navController?.navigate("home") {
                        popUpTo("task_detail/{$taskId}") { inclusive = true }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }


            },
            onDismiss = { openDeleteSheet = false },
            DeleteType.TASK
        )
    }
}