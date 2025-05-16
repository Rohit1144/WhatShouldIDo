package com.example.fit5046_g4_whatshouldido.ui.components

import com.github.mikephil.charting.formatter.ValueFormatter

class MonthAxisFormatter : ValueFormatter() {
    private val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun getFormattedValue(value: Float): String {
        val index = value.toInt().coerceIn(0, months.size - 1)
        return months[index]
    }
}
