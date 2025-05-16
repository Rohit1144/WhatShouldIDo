package com.example.fit5046_g4_whatshouldido.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Quote (
    @PrimaryKey val id: Int,
    val author: String,
    val text: String
)