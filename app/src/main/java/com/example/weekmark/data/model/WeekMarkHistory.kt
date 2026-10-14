package com.example.weekmark.data.model

import java.time.LocalDate

data class WeekMarkHistory(
    val id: String,
    val selectedDate: LocalDate,
    val weekNumber: Int,
    val weekYear: Int,
    val weekStart: LocalDate,
    val weekEnd: LocalDate,
    val calculatedAt: Long
)
