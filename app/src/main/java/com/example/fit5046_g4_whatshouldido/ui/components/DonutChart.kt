package com.example.fit5046_g4_whatshouldido.ui.components

import android.graphics.Color
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    label: String,
    percentage: Float,
    colorHex: String
) {
    AndroidView(
        factory = { context ->
            PieChart(context).apply {

                // Data Setup for Chart
                val pieEntries = listOf(
                    PieEntry(percentage, label),
                    PieEntry(100f - percentage, "")
                )

                val dataSet = PieDataSet(pieEntries, "").apply {
                    colors = listOf(
                        colorHex.toColorInt(),
                        Color.LTGRAY
                    )
                    valueTextColor = Color.TRANSPARENT
                    sliceSpace = 2f
                }

                data = PieData(dataSet)

                // Style chart
                isDrawHoleEnabled = true
                holeRadius = 70f
                transparentCircleRadius = 75f
                setUsePercentValues(true)
                description.isEnabled = false
                legend.isEnabled = false
                setDrawEntryLabels(false)

                setCenterText("${percentage.toInt()}%")
                setCenterTextSize(14f)

                animateY(2000)

                invalidate()
            }
        },
        modifier = modifier.width(100.dp).height(100.dp)
    )
}