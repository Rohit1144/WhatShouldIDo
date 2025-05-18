package com.example.fit5046_g4_whatshouldido.ui.components


import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun AnimatedLoadingText(
    loadingText: String = "Loading",
    maxDots: Int = 4,
    intervals: Long = 300L
){
    var dotCount by remember { mutableIntStateOf(0)}

    // Animate Dot count by interval
    LaunchedEffect(Unit) {
        while(true) {
            delay(intervals)
            dotCount = (dotCount + 1) % (maxDots + 1)
        }
    }

    val animatedText = loadingText + " " + ".".repeat(dotCount)

    Text(
        text = animatedText,
        style = MaterialTheme.typography.bodyMedium,
    )
}