package com.example.fit5046_g4_whatshouldido.data.repository

import android.app.Application
import com.example.fit5046_g4_whatshouldido.data.local.dao.TaskDAO
import com.example.fit5046_g4_whatshouldido.data.local.db.AppDatabase
import com.example.fit5046_g4_whatshouldido.data.local.entity.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(application: Application) {

    private var taskDao: TaskDAO = AppDatabase.getDatabase(application).taskDao()

    // Todo: Auth and User
    private val userId = "current_user_id"
    val allTasks: Flow<List<Task>> = taskDao.getAll(userId)

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    suspend fun getTaskById(id: String): Task? {
        return taskDao.getById(id)
    }

}
