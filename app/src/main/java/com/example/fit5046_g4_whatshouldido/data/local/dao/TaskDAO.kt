package com.example.fit5046_g4_whatshouldido.data.local.dao

import androidx.room.*
import com.example.fit5046_g4_whatshouldido.data.local.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {
    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getById(id: String): Task?

    @Query(
        """SELECT * FROM task
           WHERE ownerUid = :uid
           ORDER BY priority DESC"""
    )
    fun getAll(uid: String): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE pendingSync = 1 AND ownerUid = :uid")
    suspend fun getAllPendingSyncTasks(uid: String): List<Task>

    @Query("UPDATE task SET pendingSync = 0 WHERE id IN(:ids)")
    suspend fun markAsSynced(ids: List<String>)
}