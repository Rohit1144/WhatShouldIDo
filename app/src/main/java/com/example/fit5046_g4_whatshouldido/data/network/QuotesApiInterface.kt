package com.example.fit5046_g4_whatshouldido.data.network

import com.example.fit5046_g4_whatshouldido.models.QuoteModel
import retrofit2.http.GET

interface QuoteApiInterface {
    @GET("random")
    suspend fun getRandomQuote(): List<QuoteModel>
}