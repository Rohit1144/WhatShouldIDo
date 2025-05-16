package com.example.fit5046_g4_whatshouldido.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046_g4_whatshouldido.data.local.db.AppDatabase
import com.example.fit5046_g4_whatshouldido.data.repository.QuotesRepository
import com.example.fit5046_g4_whatshouldido.models.QuoteModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuotesRepository(AppDatabase.getDatabase(application).quoteDao())

    private val _quote = MutableStateFlow(QuoteModel(q= "Loading inspiration...", a= ""))
    val quote: StateFlow<QuoteModel> = _quote

    init {
        fetchQuote()
    }

    private fun fetchQuote() {
        viewModelScope.launch {
            val result = repository.getNewQuote()
            _quote.value = result?: QuoteModel(q= "Could not load quote. Try again later.", a= "")
        }
    }

    fun saveQuote(quote: QuoteModel) {
        viewModelScope.launch {
            repository.saveQuote(quote)
        }
    }

    fun deleteQuote(quote: QuoteModel) {
        viewModelScope.launch {
            repository.deleteQuote(quote)
        }
    }
}
