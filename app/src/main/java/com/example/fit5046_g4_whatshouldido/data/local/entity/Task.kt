package com.example.fit5046_g4_whatshouldido.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.identity.util.UUID

@Entity
data class TaskEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String? = null,
    val status: TaskStatus = TaskStatus.PENDING,   // PENDING / DONE / CANCELLED
    val priority: Int = 3,                         // 1 – 5
    val ownerUid: String,                          // owner user id, to handle multi-user on same device
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = createdAt,
    val completedAt: Long? = null,
    val cancelledAt: Long? = null,
    val pendingSync: Boolean = true
)

enum class TaskStatus { PENDING, DONE, CANCELLED }
