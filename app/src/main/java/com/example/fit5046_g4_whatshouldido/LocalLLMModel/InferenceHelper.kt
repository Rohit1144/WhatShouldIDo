package com.example.fit5046_g4_whatshouldido.LocalLLMModel

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import java.io.File
import java.io.FileOutputStream

object GemmaLocalInference {

    private var llmInference: LlmInference? = null

    fun initialize(context: Context) {
        val modelPath = "/sdcard/Download/gemma2-2b-it-cpu-int8.task"


        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath)
            .setMaxTopK(64)
            .build()

        llmInference = LlmInference.createFromOptions(context, options)
    }

    fun generate(prompt: String): String {
        return try {
            llmInference?.generateResponse(prompt) ?: "Model returned no output."
        } catch (e: Exception) {
            "Error during inference: ${e.localizedMessage}"
        }
    }

    fun copyModelFromAssets(context: Context, fileName: String): File {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            context.assets.open(fileName).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file
    }
}
