package com.example.fit5046_g4_whatshouldido.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.fit5046_g4_whatshouldido.data.local.entity.Quote

@Dao
interface QuoteDAO {
    @Insert
    suspend fun insert(quote: Quote)

    @Delete
    suspend fun delete(quote: Quote)

    @Query("SELECT * FROM quote")
    fun getQuotes() : List<Quote>

}