package com.example.fit5046_g4_whatshouldido.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046_g4_whatshouldido.LocalLLMModel.GemmaLocalInference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AiViewModel(application: Application) : AndroidViewModel(application){
    val isInitialized = mutableStateOf(GemmaLocalInference.isReady())

    private val _response = mutableStateOf("No input yet.")
    val response: State<String> = _response

    fun generate(prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val output = GemmaLocalInference.generate(prompt)
            _response.value = output
        }
    }
}