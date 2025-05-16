//package com.example.fit5046_g4_whatshouldido.ui.components
//
//import android.graphics.Color
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.graphics.toColorInt
//import com.example.fit5046_g4_whatshouldido.Managers.MonthlyTaskStatus
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.charts.HorizontalBarChart
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntry
//
//@Composable
//fun MonthlyBarChart(
//    modifier: Modifier = Modifier,
//    monthlyData: List<MonthlyTaskStatus>,
//){
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            HorizontalBarChart(context).apply {
//                val completedEntries = mutableListOf<BarEntry>()
//                val pendingEntries = mutableListOf<BarEntry>()
//                val cancelledEntries = mutableListOf<BarEntry>()
//
//                monthlyData.forEachIndexed{ index, status ->
//                    completedEntries.add(BarEntry(index.toFloat(), status.completed.toFloat()))
//                    pendingEntries.add(BarEntry(index.toFloat(), status.pending.toFloat()))
//                    cancelledEntries.add(BarEntry(index.toFloat(), status.cancelled.toFloat()))
//                }
//
//                val completedSet = BarDataSet(completedEntries, "Completed").apply {
//                    color = "#7FE1AD".toColorInt()
//                }
//                val pendingSet = BarDataSet(pendingEntries, "Pending").apply {
//                    color = "#F85F6A".toColorInt()
//                }
//                val cancelledSet = BarDataSet(cancelledEntries, "Cancelled").apply {
//                    color = "#5F6AF8".toColorInt()
//                }
//
//                val barData = BarData(completedSet, pendingSet, cancelledSet).apply {
//                    barWidth = 0.2f
//                    groupBars(0f, 0.2f, 0.05f) // group start, group spacing, bar spacing
//                }
//
//                data = barData
//
//                // Chart setup
//                setTouchEnabled(false)
//                setDrawGridBackground(false)
//                description.isEnabled = false
//                legend.apply {
//                    isEnabled = true
//                    yOffset = 20f
//                    textSize = 12f
//                }
//                setDrawValueAboveBar(false)
//                completedSet.setDrawValues(false)
//                pendingSet.setDrawValues(false)
//                cancelledSet.setDrawValues(false)
//
//                // X-Axis setup
//                xAxis.position = XAxis.XAxisPosition.BOTTOM
//                xAxis.setDrawGridLines(false)
//                xAxis.axisMinimum = 0f
//                xAxis.granularity = 1f
//                xAxis.labelCount = 12
//                xAxis.valueFormatter = MonthAxisFormatter()
//
//                axisLeft.setDrawGridLines(false)
//                axisRight.isEnabled = false
//
//
//                animateY(4000)
//
//                invalidate()
//            }
//        }
//    )
//}