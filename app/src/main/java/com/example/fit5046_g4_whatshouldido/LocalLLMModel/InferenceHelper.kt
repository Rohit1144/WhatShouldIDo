//package com.example.fit5046_g4_whatshouldido.LocalLLMModel
//
//import android.content.Context
//import com.google.mediapipe.tasks.genai.llminference.LlmInference
//import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
//import java.io.File
//import java.io.FileOutputStream
//
//object GemmaLocalInference {
//
//    private var llm: LlmInference? = null
//    fun initialize(context: Context) {
//
//            val modelPath = File(
//                context.getExternalFilesDir(null),
//                "gemma-2b-it-cpu-int4.bin"
//            )
//
//            if (!modelPath.exists()) {
//                throw IllegalStateException("Model file not found at ${modelPath.absolutePath}")
//            }
//
//            val options = LlmInferenceOptions.builder()
//                .setModelPath(modelPath.absolutePath)
//                .setMaxTopK(64)
//                .build()
//
//            llm = LlmInference.createFromOptions(context, options)
//        }
//
//        fun generate(prompt: String): String {
//            val result = llm?.generateResponse(prompt)
//            return result ?: "No response."
//        }
//
//        fun close() {
//            llm?.close()
//        }
//
//    fun copyModelFromAssets(context: Context, fileName: String): File {
//        val file = File(context.filesDir, fileName)
//        if (!file.exists()) {
//            context.assets.open(fileName).use { input ->
//                FileOutputStream(file).use { output ->
//                    input.copyTo(output)
//                }
//            }
//        }
//        return file
//    }
//}
