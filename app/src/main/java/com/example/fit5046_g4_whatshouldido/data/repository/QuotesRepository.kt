package com.example.fit5046_g4_whatshouldido.data.repository

import com.example.fit5046_g4_whatshouldido.data.local.dao.QuoteDAO
import com.example.fit5046_g4_whatshouldido.data.local.entity.Quote
import com.example.fit5046_g4_whatshouldido.data.network.RetrofitObject
import com.example.fit5046_g4_whatshouldido.models.QuoteModel

class QuotesRepository  (private val dao: QuoteDAO)
{
    suspend fun getNewQuote(): QuoteModel? {
        return try {
            val result = RetrofitObject.api.getRandomQuote()
            result.firstOrNull() // returns first quote or null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveQuote(quote: QuoteModel) {
        dao.insert(Quote(text = quote.q, author = quote.a))
    }

    suspend fun deleteQuote(quote: Quote) {
        dao.delete(quote)
    }
}