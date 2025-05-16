package com.example.fit5046_g4_whatshouldido.Managers

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TaskSummary(
    val completed: Int,
    val pending: Int,
    val cancelled: Int
) {
    val total: Int get() = completed + pending + cancelled

    fun percentageOfCompleted(): Float = (completed.toFloat() / total) * 100f
    fun percentageOfPending(): Float = (pending.toFloat() / total) * 100f
    fun percentageOfCancelled(): Float = (cancelled.toFloat() / total) * 100f
}

data class MonthlyTaskStatus(
    val month: Int,
    val completed: Int,
    val pending: Int,
    val cancelled: Int
)

data class TaskDetail(
    val title:String,
    val description:String,
    val status:String
)

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

    suspend fun getTaskDetail(taskId: String):TaskDetail?{
        if(user == null) return null

        val doc = db.collection("Users")
            .document(user.uid)
            .collection("tasks")
            .document(taskId)
            .get()
            .await()

        return TaskDetail (
            title = doc.getString("title") ?: "",
            description = doc.getString("description") ?: "",
            status = doc.getString("status") ?: ""
        )

    }

    suspend fun getTaskSummary(): TaskSummary {
        if(user == null) return TaskSummary(0,0,0)

        val snapshot = db.collection("Users")
            .document(user.uid)
            .collection("tasks")
            .get()
            .await()

        val tasks = snapshot.documents.mapNotNull { it.data }

        return TaskSummary(
            completed = tasks.count { it["status"] == "DONE" },
            pending = tasks.count { it["status"] == "PENDING" },
            cancelled = tasks.count { it["status"] == "CANCELED" }
        )
    }

    suspend fun getMonthlyTaskStatus(): List<MonthlyTaskStatus> {
        if(user == null) return emptyList()

        // get the tasks
        val snapshot = db.collection("Users")
            .document(user.uid)
            .collection("tasks")
            .get()
            .await()

        val tasks = snapshot.documents.mapNotNull { it.data }


        // Map month from Jan to Dec, triple - completed, pending, cancelled
        val monthlyMap = mutableMapOf<Int, Triple<Int, Int, Int>>()

        tasks.forEach{ task ->
            val createdAt = task["createdAt"] as? String ?: return@forEach
            val status = task["status"] as? String ?: return@forEach

            try{
                // Parse "createdAt" to extract month
                val month = LocalDateTime.parse(createdAt, formatter).monthValue
                val current = monthlyMap[month] ?: Triple(0,0,0)
                val updated = when(status) {
                    "DONE" -> current.copy(first = current.first + 1)
                    "PENDING" -> current.copy(second = current.second + 1)
                    "CANCELED" -> current.copy(third = current.third + 1)
                    else -> current
                }

                monthlyMap[month] = updated

            } catch(e: Exception) {
                Log.w("TaskManager", "Invalid date format for task: $createdAt", e)
            }

        }
        return(1..12).map { month ->
            val(completed, pending, cancelled) = monthlyMap[month] ?: Triple(0,0,0)
            MonthlyTaskStatus(month, completed, pending, cancelled)
        }
    }

    // For Testing the Monthly Report Chart - Remove this later (needed to populate the data)
    suspend fun seedTestTasksForMonthlyReport() {
        val tasksRef = db.collection("Users").document(user!!.uid).collection("tasks")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val testTasks = listOf(
            Triple("2025-01-10 09:00:00", "DONE", "Task 1"),
            Triple("2025-01-15 09:00:00", "PENDING", "Task 2"),
            Triple("2025-02-05 10:00:00", "CANCELED", "Task 3"),
            Triple("2025-02-20 14:00:00", "DONE", "Task 4"),
            Triple("2025-03-01 08:00:00", "PENDING", "Task 5"),
            Triple("2025-03-15 16:00:00", "DONE", "Task 6"),
            Triple("2025-03-22 12:00:00", "CANCELED", "Task 7"),
            Triple("2025-04-10 13:00:00", "DONE", "Task 8"),
            Triple("2025-05-10 13:00:00", "DONE", "Task 9"),
            Triple("2025-06-10 13:00:00", "PENDING", "Task 10"),
            Triple("2025-07-10 13:00:00", "DONE", "Task 11"),
            Triple("2025-08-10 13:00:00", "PENDING", "Task 12")
        )

        for ((createdAt, status, title) in testTasks) {
            val taskId = db.collection("tmp").document().id
            val task = mapOf(
                "id" to taskId,
                "title" to title,
                "description" to "Test seed",
                "status" to status,
                "createdAt" to createdAt,
                "updatedAt" to createdAt,
                "completedAt" to if (status == "DONE") createdAt else null,
                "cancelledAt" to if (status == "CANCELED") createdAt else null
            )

            tasksRef.document(taskId).set(task).await()
        }
    }

}