package com.example.fit5046_g4_whatshouldido.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.fit5046_g4_whatshouldido.data.local.entity.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDAO {
    @Insert
    suspend fun insert(quote: Quote)

    @Delete
    suspend fun delete(quote: Quote)

    @Query("SELECT * FROM quote WHERE userId = :userId")
    suspend fun getQuotes(userId: String) : List<Quote>

    @Query("DELETE FROM quote WHERE text = :text AND author = :author AND userId = :userId")
    suspend fun deleteByTextAndAuthor(text: String, author: String, userId: String)

    @Query("SELECT * FROM quote WHERE userId = :userId")
    fun observeAllQuotes(userId: String) : Flow<List<Quote>>
}