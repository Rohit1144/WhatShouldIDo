package com.example.fit5046_g4_whatshouldido.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.loginpagetutorial.components.BottomNavBar
import com.example.loginpagetutorial.components.TopBar
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color as AndroidColor
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData

@Composable
fun Report(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController = navController, showProfileIcon = true) },
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header
            Text(
                text = "Report Analysis",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Implement Lazy Column for infinite scroll
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth()

            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Task State",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Right
                            )
                            Divider(
                                color = Color.DarkGray,
                                thickness = 2.dp,
                                modifier = Modifier
                                    .width(100.dp)
                                    .padding(top = 4.dp)
                                    .align(Alignment.End)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        // TODO: Need to refactor this code
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            DonutChart(label = "Completed", percentage = 24f, colorHex = "#7FE1AD")
                            Spacer(Modifier.height(4.dp))
                            Text("Completed", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            DonutChart(label = "Overdue", percentage = 30f, colorHex = "#F85F6A")
                            Spacer(Modifier.height(4.dp))
                            Text("Overdue", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            DonutChart(label = "Cancelled", percentage = 46f, colorHex = "#5F6AF8")
                            Spacer(Modifier.height(4.dp))
                            Text("Cancelled", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Monthly Report",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Right
                            )
                            Divider(
                                color = Color.DarkGray,
                                thickness = 2.dp,
                                modifier = Modifier
                                    .width(100.dp)
                                    .padding(top = 4.dp)
                                    .align(Alignment.End)
                            )
                        }
                    }
//                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF9F9F9))
                    ) {
                        // TODO: refactor this
                        MonthlyBarChart(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            barValues = listOf(3f,5f,4f,7f,2f,6f,4f,5f,8f,7f,4f,3f,6f)
                        )
                    }
                }
            }
        }
    }
}

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
                        AndroidColor.LTGRAY
                    )
                    valueTextColor = AndroidColor.TRANSPARENT
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

                invalidate()
            }
        },
        modifier = modifier.width(100.dp).height(100.dp)
    )
}

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
                    valueTextColor = AndroidColor.TRANSPARENT
                    barShadowColor = AndroidColor.LTGRAY
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

                invalidate()
            }
        }
    )
}