package com.example.fit5046_g4_whatshouldido.ui.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import com.example.fit5046_g4_whatshouldido.Managers.MonthlyTaskStatus
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@Composable
fun MonthlyBarChart(
    modifier: Modifier = Modifier,
    monthlyData: List<MonthlyTaskStatus>,
){
    AndroidView(
        modifier = modifier.fillMaxWidth().height(700.dp),
        factory = { context ->
            HorizontalBarChart(context).apply {
                // Stack bar entries setup
                val entries = monthlyData.mapIndexed { index, month ->
                    BarEntry(
                        index.toFloat(), floatArrayOf(
                            month.completed.toFloat(),
                            month.pending.toFloat(),
                            month.cancelled.toFloat()
                        )
                    )
                }

                // Create single stacked dataset
                val dataSet = BarDataSet(entries, "").apply {
                    setColors(
                        "#7FE1AD".toColorInt(), // Completed
                        "#F85F6A".toColorInt(), // Pending
                        "#5F6AF8".toColorInt()  // Cancelled
                    )
                    stackLabels = arrayOf("Completed", "Pending", "Cancelled")
                    setDrawValues(false)
                }

                // Create BarData and assign
                val barData = BarData(dataSet).apply{
                    barWidth = 0.4f
                }

                data = barData

                // Chart Styling
                setDrawGridBackground(false)
                setTouchEnabled(false)
                description.isEnabled = false
                setDrawValueAboveBar(false)
                extraBottomOffset = 16f
                extraTopOffset = 16f
                animateY(4000)

                // Setting up legend
                legend.apply {
                    isEnabled = true
                    textSize = 12f
                    yOffset = 12f
                    xEntrySpace = 20f
                    formToTextSpace = 8f
                }

                // Step 6: X Axis = Months
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    valueFormatter = MonthAxisFormatter()
                    textSize = 10f
                    setLabelCount(12, true)
                }

                // Step 7: Y Axes
                axisLeft.isEnabled = false
                axisRight.apply {
                    axisMinimum = 0f
                    setDrawGridLines(false)
                }

                invalidate()
            }

        }
    )
}