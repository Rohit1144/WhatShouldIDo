package com.example.fit5046_g4_whatshouldido.Managers

import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskManager {

    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val taskDate = LocalDateTime.now().format(formatter)

    suspend fun createExampleTasks(navController: NavController, profession: String) {

        val tasksRef = db.collection("Users").document(user!!.uid).collection("tasks")

        // Define example tasks
        val exampleTitles = when (profession) {
            "Student" -> listOf(
                "Review lecture notes",
                "Submit assignment draft",
                "Join study group"
            )

            "Professional" -> listOf(
                "Prepare for team meeting",
                "Review project proposal",
                "Send weekly report"
            )

            "Freelancer" -> listOf(
                "Email client proposal",
                "Update portfolio",
                "Schedule invoice reminders"
            )

            else -> emptyList()
        }

        // Write each task as a map
        for (title in exampleTitles) {
            val taskId = db.collection("tmp").document().id // generate unique ID
            val task = mapOf(
                "id" to taskId,
                "title" to title,
                "description" to "",
                "status" to "PENDING",
                "createdAt" to taskDate,
                "updatedAt" to taskDate,
                "completedAt" to null,
                "cancelledAt" to null
            )
            tasksRef.document(taskId).set(task).await()
        }

        navController.navigate("home") {
            popUpTo("on_boarding") { inclusive = true }
        }
    }

    suspend fun addTask(title: String, description: String) {
        if (user != null) {
            val taskId = db.collection("tmp").document().id

            val task = mapOf(
                "id" to taskId,
                "title" to title,
                "description" to description,
                "status" to "PENDING",
                "createdAt" to taskDate,
                "updatedAt" to taskDate,
                "completedAt" to null,
                "cancelledAt" to null
            )

            db.collection("Users")
                .document(user.uid)
                .collection("tasks")
                .document(taskId)
                .set(task)
                .await()

        }
    }

    suspend fun deleteTask(taskId: String) {
        db.collection("Users")
            .document(user!!.uid)
            .collection("tasks")
            .document(taskId)
            .delete()
            .await()
    }

    suspend fun updateTaskStatusToCancel(toggleCancel: String, taskId: String) {

        val cancelledAt = if (toggleCancel == "CANCELED") taskDate else null

        db.collection("Users")
            .document(user!!.uid)
            .collection("tasks")
            .document(taskId)
            .update(
                mapOf(
                    "status" to toggleCancel,
                    "updatedAt" to taskDate,
                    "cancelledAt" to cancelledAt
                )
            )
            .await()
    }

    suspend fun updateTaskStatusToDone(toggleStatus: String, taskId: String, updatedAt: String) {
        val completedAt = if(toggleStatus == "DONE") updatedAt else null

        db.collection("Users")
            .document(user!!.uid)
            .collection("tasks")
            .document(taskId)
            .update(
                mapOf(
                    "status" to toggleStatus,
                    "updatedAt" to updatedAt,
                    "completedAt" to completedAt
                )
            ).await()

    }

    suspend fun updateTaskDetails(title: String, description: String, taskId: String) {

        db.collection("Users")
            .document(user!!.uid)
            .collection("tasks")
            .document(taskId)
            .update(
                mapOf(
                    "title" to title,
                    "description" to description,
                    "updatedAt" to taskDate
                )
            )
            .await()
    }

    suspend fun getTaskList(): List<Map<String, Any?>> {
        if(user == null) return emptyList()

        val snapshot = db.collection("Users")
            .document(user.uid)
            .collection("tasks")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.data }
    }

}