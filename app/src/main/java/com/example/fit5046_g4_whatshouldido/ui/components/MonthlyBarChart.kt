package com.example.fit5046_g4_whatshouldido.ui.components

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@Composable
fun MonthlyBarChart(
    modifier: Modifier = Modifier,
    barValues: List<Float>,
){
    AndroidView(
        modifier = modifier,
        factory = { context ->
            BarChart(context).apply {
                val entries = barValues.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }

                val dataSet = BarDataSet(entries, "").apply {
                    colors = listOf("#F85F6A".toColorInt())
                    valueTextColor = Color.TRANSPARENT
                    barShadowColor = Color.LTGRAY
                }

                data = BarData(dataSet).apply{
                    barWidth = 0.4f
                }

                setTouchEnabled(false)
                setDrawGridBackground(false)
                description.isEnabled = false
                legend.isEnabled = false
                setDrawValueAboveBar(false)

                // Axis Setup
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(false)
                xAxis.setDrawLabels(false)

                axisLeft.setDrawGridLines(false)
                axisLeft.setDrawAxisLine(false)
                axisLeft.setDrawLabels(false)

                axisRight.isEnabled = false

                animateY(4000)

                invalidate()
            }
        }
    )
}