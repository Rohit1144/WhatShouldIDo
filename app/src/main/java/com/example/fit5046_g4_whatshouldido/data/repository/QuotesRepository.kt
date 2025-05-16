package com.example.fit5046_g4_whatshouldido.data.repository

import com.example.fit5046_g4_whatshouldido.data.network.RetrofitObject
import com.example.fit5046_g4_whatshouldido.models.QuoteModel

class QuotesRepository {
    suspend fun getNewQuote(): QuoteModel? {
        return try {
            val result = RetrofitObject.api.getRandomQuote()
            result.firstOrNull() // returns first quote or null
        } catch (e: Exception) {
            null
        }
    }
}