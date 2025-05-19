package com.example.fit5046_g4_whatshouldido.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Quote (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val author: String,
    val text: String,
    val userId: String
)