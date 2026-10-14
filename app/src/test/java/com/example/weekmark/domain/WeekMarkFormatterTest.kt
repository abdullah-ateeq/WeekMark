package com.example.weekmark.domain

import com.example.weekmark.data.model.WeekMarkHistory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class WeekMarkFormatterTest {

    @Test
    fun testFormatWeekNumberTwoDigits() {
        assertEquals("W01", WeekMarkFormatter.formatWeekNumber(1))
        assertEquals("W09", WeekMarkFormatter.formatWeekNumber(9))
        assertEquals("W10", WeekMarkFormatter.formatWeekNumber(10))
        assertEquals("W42", WeekMarkFormatter.formatWeekNumber(42))
        assertEquals("W52", WeekMarkFormatter.formatWeekNumber(52))
        assertEquals("W53", WeekMarkFormatter.formatWeekNumber(53))
    }

    @Test
    fun testFormatWeekRangeCrossingYearBoundary() {
        val weekStart = LocalDate.of(2026, 12, 28)
        val weekEnd = LocalDate.of(2027, 1, 3)
        val compactRange = WeekMarkFormatter.formatCompactWeekRange(weekStart, weekEnd)

        // Must display both years when crossing year boundaries
        assertTrue(compactRange.contains("2026"))
        assertTrue(compactRange.contains("2027"))
        assertEquals("28 Dec 2026 — 3 Jan 2027", compactRange)
    }

    @Test
    fun testFormatWeekRangeSameYear() {
        val weekStart = LocalDate.of(2026, 10, 12)
        val weekEnd = LocalDate.of(2026, 10, 18)
        val compactRange = WeekMarkFormatter.formatCompactWeekRange(weekStart, weekEnd)
        assertEquals("12 — 18 Oct 2026", compactRange)
    }

    @Test
    fun testDayPositionFormatting() {
        assertEquals("DAY 1 OF 7", WeekMarkFormatter.formatDayPosition(1))
        assertEquals("DAY 3 OF 7", WeekMarkFormatter.formatDayPosition(3))
        assertEquals("DAY 7 OF 7", WeekMarkFormatter.formatDayPosition(7))
    }

    @Test
    fun testDaysAfterAndIncludingToday() {
        assertEquals("4 DAYS AFTER TODAY", WeekMarkFormatter.formatDaysAfterToday(4))
        assertEquals("1 DAY AFTER TODAY", WeekMarkFormatter.formatDaysAfterToday(1))
        assertEquals("5 DAYS INCLUDING TODAY", WeekMarkFormatter.formatDaysIncludingToday(5))
        assertEquals("1 DAY INCLUDING TODAY", WeekMarkFormatter.formatDaysIncludingToday(1))
    }

    @Test
    fun testGenerateCopyText() {
        val date = LocalDate.of(2026, 10, 14) // Wednesday
        val result = WeekMarkCalculator.calculate(date)
        val copyText = WeekMarkFormatter.generateCopyText(result)

        assertTrue(copyText.contains("WeekMark Result"))
        assertTrue(copyText.contains("Date: Wednesday, 14 October 2026"))
        assertTrue(copyText.contains("ISO week: W42"))
        assertTrue(copyText.contains("ISO week year: 2026"))
        assertTrue(copyText.contains("Week start: Monday, 12 October 2026"))
        assertTrue(copyText.contains("Week end: Sunday, 18 October 2026"))
        assertTrue(copyText.contains("Day position: 3 of 7"))
        assertTrue(copyText.contains("Day of year: 287"))
    }

    @Test
    fun testGenerateCopyTextWithYearDifference() {
        val date = LocalDate.of(2025, 12, 29)
        val result = WeekMarkCalculator.calculate(date)
        val copyText = WeekMarkFormatter.generateCopyText(result)

        assertTrue(copyText.contains("Calendar year: 2025"))
        assertTrue(copyText.contains("Note: ISO week year differs from calendar year"))
    }

    @Test
    fun testGenerateShareText() {
        val date = LocalDate.of(2026, 10, 14)
        val result = WeekMarkCalculator.calculate(date)
        val shareText = WeekMarkFormatter.generateShareText(result)

        assertTrue(shareText.contains("WeekMark"))
        assertTrue(shareText.contains("Wednesday, 14 October 2026"))
        assertTrue(shareText.contains("ISO Week: W42"))
        assertTrue(shareText.contains("ISO Week Year: 2026"))
        assertTrue(shareText.contains("Day: 3 of 7"))
    }

    @Test
    fun testFormatHistoryItemRange() {
        val history = WeekMarkHistory(
            id = "test-1",
            selectedDate = LocalDate.of(2026, 10, 14),
            weekNumber = 42,
            weekYear = 2026,
            weekStart = LocalDate.of(2026, 10, 12),
            weekEnd = LocalDate.of(2026, 10, 18),
            calculatedAt = 1000L
        )
        val formattedRange = WeekMarkFormatter.formatHistoryItemRange(history)
        assertEquals("12 — 18 Oct 2026", formattedRange)
    }
}
