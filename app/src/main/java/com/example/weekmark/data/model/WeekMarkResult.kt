package com.example.weekmark.data.model

import java.time.DayOfWeek
import java.time.LocalDate

data class WeekMarkResult(
    val selectedDate: LocalDate,
    val dayOfWeek: DayOfWeek,
    val dayOfWeekNumber: Int,
    val weekNumber: Int,
    val weekYear: Int,
    val weekStart: LocalDate,
    val weekEnd: LocalDate,
    val calendarYear: Int,
    val dayOfYear: Int,
    val daysAfterTodayUntilWeekEnd: Int,
    val daysIncludingTodayUntilWeekEnd: Int,
    val weeksInIsoYear: Int,
    val isFirstWeekOfWeekYear: Boolean,
    val isLastWeekOfWeekYear: Boolean,
    val isCalendarYearDifferentFromWeekYear: Boolean
)
