package com.example.weekmark.domain

import com.example.weekmark.data.model.WeekMarkHistory
import com.example.weekmark.data.model.WeekMarkResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object WeekMarkFormatter {

    private val fullDateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault())

    private val standardDateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())

    private val mediumDateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())

    private val shortMonthDayFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())

    fun formatWeekNumber(weekNumber: Int): String {
        return String.format(Locale.getDefault(), "W%02d", weekNumber)
    }

    fun formatFullDate(date: LocalDate): String {
        return date.format(fullDateFormatter)
    }

    fun formatStandardDate(date: LocalDate): String {
        return date.format(standardDateFormatter)
    }

    fun formatMediumDate(date: LocalDate): String {
        return date.format(mediumDateFormatter)
    }

    /**
     * Formats the week range compactly.
     * When crossing year boundaries (e.g. 28 Dec 2026 to 3 Jan 2027),
     * displays both years to avoid ambiguity.
     */
    fun formatCompactWeekRange(weekStart: LocalDate, weekEnd: LocalDate): String {
        return if (weekStart.year != weekEnd.year) {
            "${weekStart.format(mediumDateFormatter)} — ${weekEnd.format(mediumDateFormatter)}"
        } else if (weekStart.month != weekEnd.month) {
            "${weekStart.format(shortMonthDayFormatter)} — ${weekEnd.format(mediumDateFormatter)}"
        } else {
            "${weekStart.dayOfMonth} — ${weekEnd.format(mediumDateFormatter)}"
        }
    }

    /**
     * Formats the full week range with full month and year names.
     */
    fun formatFullWeekRange(weekStart: LocalDate, weekEnd: LocalDate): String {
        return if (weekStart.year != weekEnd.year) {
            "${weekStart.format(standardDateFormatter)} – ${weekEnd.format(standardDateFormatter)}"
        } else {
            "${weekStart.format(standardDateFormatter)} – ${weekEnd.format(standardDateFormatter)}"
        }
    }

    fun formatDayPosition(dayOfWeekNumber: Int): String {
        return "DAY $dayOfWeekNumber OF 7"
    }

    fun formatDaysAfterToday(days: Int): String {
        return if (days == 1) "1 DAY AFTER TODAY" else "$days DAYS AFTER TODAY"
    }

    fun formatDaysIncludingToday(days: Int): String {
        return if (days == 1) "1 DAY INCLUDING TODAY" else "$days DAYS INCLUDING TODAY"
    }

    /**
     * Generates structured text for clipboard copy.
     */
    fun generateCopyText(result: WeekMarkResult): String {
        val weekStr = formatWeekNumber(result.weekNumber)
        val fullDate = formatFullDate(result.selectedDate)
        val startDate = formatFullDate(result.weekStart)
        val endDate = formatFullDate(result.weekEnd)

        val builder = StringBuilder()
        builder.appendLine("WeekMark Result")
        builder.appendLine()
        builder.appendLine("Date: $fullDate")
        builder.appendLine("ISO week: $weekStr")
        builder.appendLine("ISO week year: ${result.weekYear}")
        builder.appendLine("Week start: $startDate")
        builder.appendLine("Week end: $endDate")
        builder.appendLine("Day position: ${result.dayOfWeekNumber} of 7")
        builder.appendLine("Day of year: ${result.dayOfYear}")

        if (result.isCalendarYearDifferentFromWeekYear) {
            builder.appendLine("Calendar year: ${result.calendarYear}")
            builder.appendLine("Note: ISO week year differs from calendar year")
        }

        return builder.toString().trimEnd()
    }

    /**
     * Generates text for native sharing.
     */
    fun generateShareText(result: WeekMarkResult): String {
        val weekStr = formatWeekNumber(result.weekNumber)
        val fullDate = formatFullDate(result.selectedDate)
        val weekRange = formatFullWeekRange(result.weekStart, result.weekEnd)

        val builder = StringBuilder()
        builder.appendLine("WeekMark")
        builder.appendLine()
        builder.appendLine(fullDate)
        builder.appendLine()
        builder.appendLine("ISO Week: $weekStr")
        builder.appendLine("ISO Week Year: ${result.weekYear}")
        builder.appendLine("Week: $weekRange")
        builder.appendLine("Day: ${result.dayOfWeekNumber} of 7")

        if (result.isCalendarYearDifferentFromWeekYear) {
            builder.appendLine("Calendar Year: ${result.calendarYear}")
        }

        return builder.toString().trimEnd()
    }

    fun formatHistoryItemRange(history: WeekMarkHistory): String {
        return formatCompactWeekRange(history.weekStart, history.weekEnd)
    }
}
