package com.example.fit5046_g4_whatshouldido.Managers

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskManager {
    suspend fun createExampleTasks() {
        val user = Firebase.auth.currentUser ?: return
        val db = Firebase.firestore

        // Fetch profession from onboardingValues
        val doc = db.collection("Users").document(user.uid).get().await()
        val profession = doc.get("onboardingValues.profession") as? String ?: return

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)
        val tasksRef = db.collection("Users").document(user.uid).collection("tasks")

        // Define example tasks
        val exampleTitles = when (profession) {
            "Student" -> listOf("Review lecture notes", "Submit assignment draft", "Join study group")
            "Professional" -> listOf("Prepare for team meeting", "Review project proposal", "Send weekly report")
            "Freelancer" -> listOf("Email client proposal", "Update portfolio", "Schedule invoice reminders")
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
                "createdAt" to current,
                "updatedAt" to current,
                "completedAt" to null,
                "cancelledAt" to null
            )
            tasksRef.document(taskId).set(task).await()
        }
    }
}