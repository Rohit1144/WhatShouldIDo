package com.example.fit5046_g4_whatshouldido

import android.app.Application
import android.util.Log
import com.example.fit5046_g4_whatshouldido.LocalLLMModel.GemmaLocalInference

class AppEngine : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread {
            try {
                GemmaLocalInference.initialize(this)
                Log.d("AppEngine", "Gemma model initialized on app start")
            } catch( e: Exception) {
                Log.e("AppEngine", "Model init failed: ${e.message}")
            }
        }.start()
    }
}