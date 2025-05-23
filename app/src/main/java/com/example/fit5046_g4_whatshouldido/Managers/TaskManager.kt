package com.example.fit5046_g4_whatshouldido.Managers

import android.util.Log
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
    val status:String,
    val dueAt:String
)

class TaskManager {

    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val formatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val taskDate: String? = LocalDateTime.now().format(formatter)
    val sampleDueDateTime: String? = LocalDateTime.now().plusDays(1).format(formatter)

    suspend fun createExampleTasks(profession: String) {
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
                "isArchived" to false,
                "createdAt" to taskDate,
                "updatedAt" to taskDate,
                "completedAt" to null,
                "cancelledAt" to null,
                "dueAt" to sampleDueDateTime
            )
            tasksRef.document(taskId).set(task).await()
        }
    }

    suspend fun addTask(title: String, description: String, dueDateTime: String) {
        if (user != null) {
            val taskId = db.collection("tmp").document().id

            val task = mapOf(
                "id" to taskId,
                "title" to title,
                "description" to description,
                "status" to "PENDING",
                "isArchived" to false,
                "createdAt" to taskDate,
                "updatedAt" to taskDate,
                "completedAt" to null,
                "cancelledAt" to null,
                "dueAt" to dueDateTime
            )

            db.collection("Users")
                .document(user.uid)
                .collection("tasks")
                .document(taskId)
                .set(task)
                .await()

        }
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

    suspend fun updateTaskDetails(title: String, description: String, dueDateTime: String, taskId: String) {

        db.collection("Users")
            .document(user!!.uid)
            .collection("tasks")
            .document(taskId)
            .update(
                mapOf(
                    "title" to title,
                    "description" to description,
                    "updatedAt" to taskDate,
                    "dueAt" to dueDateTime
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

    suspend fun getPendingTaskList(): List<Pair<String, String>> {
        // Check if the user is logged in
        if (user == null) return emptyList()

        return try {
            // Fetch all tasks from the Firestore database
            val snapshot = db.collection("Users")
                .document(user.uid)
                .collection("tasks")
                .get()
                .await()

            // Process each document and filter only pending tasks
            snapshot.documents.mapNotNull { doc ->
                val title = doc.getString("title") ?: return@mapNotNull null
                val status = doc.getString("status") ?: return@mapNotNull null
                val taskId = doc.id

                // Check if the task is pending
                if (status.equals("PENDING", ignoreCase = true)) {
                    // Return a map with the task ID and title
                    Pair(taskId, title)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            // Handle any potential errors
            emptyList()
        }
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
            status = doc.getString("status") ?: "",
            dueAt = doc.getString("dueAt") ?: ""
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

    // archive tasks - (when user clicks bin icon archives the tasks not permanently delete it)
    suspend fun archiveTask(taskId: String) {
        if (user != null) {
            db.collection("Users")
                .document(user.uid)
                .collection("tasks")
                .document(taskId)
                .update(
                    mapOf(
                        "isArchived" to true
                    )
                )
                .await()
        }
    }

    // Delete all tasks - (when user clicks factory reset, deletes all the tasks that they added)
    // Note: Firebase does not support bulk deletion in single call -> use batch (up to 500 per batch)
    suspend fun deleteAllTasks() {
        if (user != null){
            val taskSnapshot = db.collection("Users")
                .document(user.uid)
                .collection("tasks")
                .get()
                .await()

            val batch = db.batch()

            for (task in taskSnapshot.documents) {
                batch.delete(task.reference)
            }

            batch.commit().await()
        }
    }
}