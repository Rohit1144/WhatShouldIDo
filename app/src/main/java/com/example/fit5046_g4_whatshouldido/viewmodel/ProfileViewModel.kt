package com.example.fit5046_g4_whatshouldido.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046_g4_whatshouldido.data.local.db.AppDatabase
import com.example.fit5046_g4_whatshouldido.data.local.entity.Quote
import com.example.fit5046_g4_whatshouldido.data.repository.QuotesRepository
import com.example.fit5046_g4_whatshouldido.models.QuoteModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//TODO: change the name to quoteviewmodel
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val user = Firebase.auth.currentUser ?: error("No user found")
    private val userId = user.uid

    private val repository = QuotesRepository(
        AppDatabase.getDatabase(application).quoteDao(), userId
    )

    private val _quote = MutableStateFlow(QuoteModel(q= "Loading inspiration...", a= ""))
    val quote: StateFlow<QuoteModel> = _quote

    val quotes: StateFlow<List<Quote>> =
        repository.observeAllQuotes()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    init {
        fetchQuote()
    }

    fun fetchQuote() {
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
