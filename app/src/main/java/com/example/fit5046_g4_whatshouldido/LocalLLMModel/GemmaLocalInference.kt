package com.example.fit5046_g4_whatshouldido.LocalLLMModel

import android.content.Context
import android.util.Log
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
import java.io.File

object GemmaLocalInference {
    private var llmInference: LlmInference? = null
    private var initialized = false

    fun initialize(context: Context) {
        if(initialized) return

        val modelFile = File(context.getExternalFilesDir(null),
        "gemma-2b-it-cpu-int4.bin"
        )

        if(!modelFile.exists()) {
            throw IllegalStateException("Model file not found at ${modelFile.absolutePath}")
        }

        val options = LlmInferenceOptions.builder()
            .setModelPath(modelFile.absolutePath)
            .setMaxTopK(64)
            .build()

        llmInference = LlmInference.createFromOptions(context, options)
        initialized = true

        Log.d("GemmaModel", "Gemma model initialized from ${modelFile.absolutePath}")
    }

    fun generate(prompt: String): String {
        return try {
            llmInference?.generateResponse(prompt) ?: "Model returned no output"
        } catch (e : Exception) {
            Log.e("GemmaModel", "Generation error: ${e.message}")
            "Error during inference: ${e.localizedMessage}"
        }
    }

    fun isReady(): Boolean = initialized
}