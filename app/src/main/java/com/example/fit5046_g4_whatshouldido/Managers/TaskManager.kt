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
    val status:String,
    val dueAt:String
)

class TaskManager {

    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val taskDate = LocalDateTime.now().format(formatter)
    val sampleDueDateTime = LocalDateTime.now().plusDays(1).format(formatter)

//    suspend fun createExampleTasks(navController: NavController, profession: String) {
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

//        navController.navigate("home") {
//            popUpTo("on_boarding") { inclusive = true }
//        }
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

//    suspend fun deleteTask(taskId: String) {
//        db.collection("Users")
//            .document(user!!.uid)
//            .collection("tasks")
//            .document(taskId)
//            .delete()
//            .await()
//    }

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

    // For Testing the Monthly Report Chart - Remove this later (needed to populate the data)
    suspend fun seedTestTasksForMonthlyReport() {
        val tasksRef = db.collection("Users").document(user!!.uid).collection("tasks")
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        data class TaskData<A, B, C, D, E>(
            val first: A,
            val second: B,
            val third: C,
            val fourth: D,
            val fifth: E
        )

        val testTasks = listOf(
            TaskData("03/01/2025 09:00", "03/01/2025 11:00", "PENDING", "Pick up parcel", "From the post office. Check the address on the message"),
            TaskData("05/01/2025 10:00", "05/01/2025 12:00", "DONE", "Submit assignment", "Upload to Moodle before noon."),
            TaskData("08/01/2025 08:00", "08/01/2025 09:00", "CANCELED", "Doctor appointment", "Cancelled due to schedule conflict."),
            TaskData("12/01/2025 14:00", "12/01/2025 15:00", "DONE", "Team meeting", "Monthly progress sync-up."),
            TaskData("15/01/2025 10:00", "15/01/2025 11:30", "PENDING", "Buy groceries", "Get eggs, milk, bread."),
            TaskData("18/01/2025 13:00", "18/01/2025 14:00", "PENDING", "Call bank", "Inquire about credit card fees."),
            TaskData("20/01/2025 09:30", "20/01/2025 10:30", "DONE", "Morning jog", "Run 5km at the park."),
            TaskData("22/01/2025 16:00", "22/01/2025 17:00", "CANCELED", "Dentist visit", "Rescheduled to next month."),
            TaskData("25/01/2025 07:30", "25/01/2025 08:30", "DONE", "Yoga session", "Stretch and meditate."),
            TaskData("28/01/2025 11:00", "28/01/2025 12:00", "PENDING", "Finish book", "Read last 3 chapters."),
            TaskData("30/01/2025 15:00", "30/01/2025 16:00", "PENDING", "Clean kitchen", "Organize pantry and fridge."),
            TaskData("02/02/2025 09:00", "02/02/2025 10:00", "DONE", "Renew license", "Bring old documents."),
            TaskData("04/02/2025 10:00", "04/02/2025 11:00", "PENDING", "Return library books", "Due today."),
            TaskData("06/02/2025 08:00", "06/02/2025 09:30", "DONE", "Morning workout", "Push-pull routine."),
            TaskData("08/02/2025 17:00", "08/02/2025 18:00", "PENDING", "Cook dinner", "Try new pasta recipe."),
            TaskData("10/02/2025 13:00", "10/02/2025 14:00", "PENDING", "Fix bike", "Chain is loose."),
            TaskData("12/02/2025 14:30", "12/02/2025 15:30", "DONE", "Plan trip", "Book accommodation."),
            TaskData("14/02/2025 11:00", "14/02/2025 12:00", "CANCELED", "Video call with John", "Meeting canceled."),
            TaskData("16/02/2025 09:00", "16/02/2025 10:00", "PENDING", "Clean desk", "Organize documents."),
            TaskData("18/02/2025 10:00", "18/02/2025 11:00", "DONE", "Walk dog", "Go to the beach."),
            TaskData("20/02/2025 16:00", "20/02/2025 17:00", "PENDING", "Grocery restock", "Buy fruits and veggies."),
            TaskData("22/02/2025 07:30", "22/02/2025 08:30", "PENDING", "Water plants", "Don’t forget balcony."),
            TaskData("24/02/2025 13:00", "24/02/2025 14:00", "DONE", "Client email", "Reply with proposal."),
            TaskData("26/02/2025 14:30", "26/02/2025 15:30", "CANCELED", "Vet appointment", "Pet got better."),
            TaskData("28/02/2025 15:00", "28/02/2025 16:00", "PENDING", "Wash car", "Interior + exterior."),
            TaskData("02/03/2025 10:00", "02/03/2025 11:00", "DONE", "Pay bills", "Gas and internet."),
            TaskData("04/03/2025 12:00", "04/03/2025 13:00", "PENDING", "Organize files", "Sort downloads folder."),
            TaskData("06/03/2025 11:30", "06/03/2025 12:30", "DONE", "Bake cake", "Birthday surprise."),
            TaskData("08/03/2025 09:30", "08/03/2025 10:30", "CANCELED", "Workshop registration", "Event postponed."),
            TaskData("10/03/2025 14:00", "10/03/2025 15:00", "PENDING", "Weekly reflection", "Journal and plan next week.")
        )

        for ((createdAtStr, dueAtStr, status, title, description) in testTasks) {
            val createdAt = LocalDateTime.parse(createdAtStr, formatter)
            val dueAt = LocalDateTime.parse(dueAtStr, formatter)

            val createdAtFormatted = formatter.format(createdAt)
            val dueAtFormatted = formatter.format(dueAt)

            val taskId = db.collection("tmp").document().id
            val task = mapOf(
                "id" to taskId,
                "title" to title,
                "description" to description,
                "status" to status,
                "isArchived" to false,
                "createdAt" to createdAtFormatted,
                "updatedAt" to createdAtFormatted,
                "completedAt" to if (status == "DONE") createdAtFormatted else null,
                "cancelledAt" to if (status == "CANCELED") createdAtFormatted else null,
                "dueAt" to dueAtFormatted
            )

            tasksRef.document(taskId).set(task).await()
        }
    }

}