package com.example.fit5046_g4_whatshouldido.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.fit5046_g4_whatshouldido.LocalLLMModel.GemmaLocalInference

class AiViewModel(application: Application) : AndroidViewModel(application){
    val isInitialized = mutableStateOf(GemmaLocalInference.isReady())

}