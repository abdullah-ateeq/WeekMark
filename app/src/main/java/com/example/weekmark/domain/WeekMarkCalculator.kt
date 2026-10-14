package com.example.weekmark.domain

import com.example.weekmark.data.model.WeekMarkResult
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

object WeekMarkCalculator {

    fun calculate(date: LocalDate): WeekMarkResult {
        val isoFields = WeekFields.ISO
        val weekNumber = date.get(isoFields.weekOfWeekBasedYear())
        val weekYear = date.get(isoFields.weekBasedYear())
        val dayOfWeek = date.dayOfWeek
        val dayOfWeekNumber = date.get(isoFields.dayOfWeek()) // Monday = 1, Sunday = 7
        val weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val weekEnd = weekStart.plusDays(6)
        val calendarYear = date.year
        val dayOfYear = date.dayOfYear
        val daysAfterTodayUntilWeekEnd = 7 - dayOfWeekNumber
        val daysIncludingTodayUntilWeekEnd = 8 - dayOfWeekNumber
        val weeksInIsoYear = calculateWeeksInIsoYear(weekYear)
        val isFirstWeekOfWeekYear = (weekNumber == 1)
        val isLastWeekOfWeekYear = (weekNumber == weeksInIsoYear)
        val isCalendarYearDifferentFromWeekYear = (calendarYear != weekYear)

        return WeekMarkResult(
            selectedDate = date,
            dayOfWeek = dayOfWeek,
            dayOfWeekNumber = dayOfWeekNumber,
            weekNumber = weekNumber,
            weekYear = weekYear,
            weekStart = weekStart,
            weekEnd = weekEnd,
            calendarYear = calendarYear,
            dayOfYear = dayOfYear,
            daysAfterTodayUntilWeekEnd = daysAfterTodayUntilWeekEnd,
            daysIncludingTodayUntilWeekEnd = daysIncludingTodayUntilWeekEnd,
            weeksInIsoYear = weeksInIsoYear,
            isFirstWeekOfWeekYear = isFirstWeekOfWeekYear,
            isLastWeekOfWeekYear = isLastWeekOfWeekYear,
            isCalendarYearDifferentFromWeekYear = isCalendarYearDifferentFromWeekYear
        )
    }

    /**
     * Calculates the total number of ISO weeks in a given ISO week year.
     * In ISO-8601, December 28 always belongs to the last ISO week of its ISO week year.
     */
    fun calculateWeeksInIsoYear(weekYear: Int): Int {
        return LocalDate.of(weekYear, 12, 28)
            .get(WeekFields.ISO.weekOfWeekBasedYear())
    }

    fun previousWeek(date: LocalDate): LocalDate = date.minusWeeks(1)
    fun nextWeek(date: LocalDate): LocalDate = date.plusWeeks(1)
    fun previousDay(date: LocalDate): LocalDate = date.minusDays(1)
    fun nextDay(date: LocalDate): LocalDate = date.plusDays(1)
    fun startOfMonth(date: LocalDate): LocalDate = date.withDayOfMonth(1)
    fun endOfMonth(date: LocalDate): LocalDate = date.with(TemporalAdjusters.lastDayOfMonth())
    fun startOfYear(date: LocalDate): LocalDate = date.withDayOfYear(1)
    fun endOfYear(date: LocalDate): LocalDate = date.with(TemporalAdjusters.lastDayOfYear())
}
