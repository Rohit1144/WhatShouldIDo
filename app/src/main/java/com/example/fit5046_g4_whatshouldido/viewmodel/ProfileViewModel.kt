package com.example.fit5046_g4_whatshouldido.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046_g4_whatshouldido.data.repository.QuotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = QuotesRepository()

    private val _quote = MutableStateFlow("Loading inspiration...")
    val quote: StateFlow<String> = _quote

    init {
        fetchQuote()
    }

    private fun fetchQuote() {
        viewModelScope.launch {
            val result = repository.getNewQuote()
            _quote.value = result?.let { "\"${it.q}\"\n– ${it.a}" }
                ?: "Could not load quote. Try again later."
        }
    }
}
