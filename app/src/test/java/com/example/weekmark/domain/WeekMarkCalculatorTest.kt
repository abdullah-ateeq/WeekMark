package com.example.weekmark.domain

import com.example.weekmark.data.model.WeekMarkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class WeekMarkCalculatorTest {

    // 1. Basic week number
    @Test
    fun testBasicWeekNumber() {
        val date = LocalDate.of(2026, 10, 14) // Wednesday
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(42, result.weekNumber)
    }

    // 2. Basic week year
    @Test
    fun testBasicWeekYear() {
        val date = LocalDate.of(2026, 10, 14)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(2026, result.weekYear)
    }

    // 3. Monday date
    @Test
    fun testMondayDate() {
        val date = LocalDate.of(2026, 10, 12)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(DayOfWeek.MONDAY, result.dayOfWeek)
        assertEquals(1, result.dayOfWeekNumber)
        assertEquals(date, result.weekStart)
        assertEquals(LocalDate.of(2026, 10, 18), result.weekEnd)
        assertEquals(6, result.daysAfterTodayUntilWeekEnd)
        assertEquals(7, result.daysIncludingTodayUntilWeekEnd)
    }

    // 4. Tuesday date
    @Test
    fun testTuesdayDate() {
        val date = LocalDate.of(2026, 10, 13)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(DayOfWeek.TUESDAY, result.dayOfWeek)
        assertEquals(2, result.dayOfWeekNumber)
        assertEquals(5, result.daysAfterTodayUntilWeekEnd)
        assertEquals(6, result.daysIncludingTodayUntilWeekEnd)
    }

    // 5. Wednesday date
    @Test
    fun testWednesdayDate() {
        val date = LocalDate.of(2026, 10, 14)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(DayOfWeek.WEDNESDAY, result.dayOfWeek)
        assertEquals(3, result.dayOfWeekNumber)
        assertEquals(4, result.daysAfterTodayUntilWeekEnd)
        assertEquals(5, result.daysIncludingTodayUntilWeekEnd)
    }

    // 6. Thursday date
    @Test
    fun testThursdayDate() {
        val date = LocalDate.of(2026, 10, 15)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(DayOfWeek.THURSDAY, result.dayOfWeek)
        assertEquals(4, result.dayOfWeekNumber)
        assertEquals(3, result.daysAfterTodayUntilWeekEnd)
        assertEquals(4, result.daysIncludingTodayUntilWeekEnd)
    }

    // 7. Friday date
    @Test
    fun testFridayDate() {
        val date = LocalDate.of(2026, 10, 16)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(DayOfWeek.FRIDAY, result.dayOfWeek)
        assertEquals(5, result.dayOfWeekNumber)
        assertEquals(2, result.daysAfterTodayUntilWeekEnd)
        assertEquals(3, result.daysIncludingTodayUntilWeekEnd)
    }

    // 8. Saturday date
    @Test
    fun testSaturdayDate() {
        val date = LocalDate.of(2026, 10, 17)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(DayOfWeek.SATURDAY, result.dayOfWeek)
        assertEquals(6, result.dayOfWeekNumber)
        assertEquals(1, result.daysAfterTodayUntilWeekEnd)
        assertEquals(2, result.daysIncludingTodayUntilWeekEnd)
    }

    // 9. Sunday date
    @Test
    fun testSundayDate() {
        val date = LocalDate.of(2026, 10, 18)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(DayOfWeek.SUNDAY, result.dayOfWeek)
        assertEquals(7, result.dayOfWeekNumber)
        assertEquals(LocalDate.of(2026, 10, 12), result.weekStart)
        assertEquals(date, result.weekEnd)
        assertEquals(0, result.daysAfterTodayUntilWeekEnd)
        assertEquals(1, result.daysIncludingTodayUntilWeekEnd)
    }

    // 10. Week start
    @Test
    fun testWeekStartCalculation() {
        val date = LocalDate.of(2026, 10, 14)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(LocalDate.of(2026, 10, 12), result.weekStart)
        assertEquals(DayOfWeek.MONDAY, result.weekStart.dayOfWeek)
    }

    // 11. Week end
    @Test
    fun testWeekEndCalculation() {
        val date = LocalDate.of(2026, 10, 14)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(LocalDate.of(2026, 10, 18), result.weekEnd)
        assertEquals(DayOfWeek.SUNDAY, result.weekEnd.dayOfWeek)
    }

    // 12. Day position
    @Test
    fun testDayPosition() {
        val wednesday = LocalDate.of(2026, 10, 14)
        val result = WeekMarkCalculator.calculate(wednesday)
        assertEquals(3, result.dayOfWeekNumber)
    }

    // 13. Day-of-year
    @Test
    fun testDayOfYear() {
        val date = LocalDate.of(2026, 10, 14)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(287, result.dayOfYear)
    }

    // 14. ISO year
    @Test
    fun testIsoYear() {
        val date = LocalDate.of(2025, 12, 29)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(2026, result.weekYear)
    }

    // 15. Calendar year
    @Test
    fun testCalendarYear() {
        val date = LocalDate.of(2025, 12, 29)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(2025, result.calendarYear)
    }

    // 16. Calendar/ISO year mismatch
    @Test
    fun testCalendarIsoYearMismatch() {
        val date = LocalDate.of(2025, 12, 29)
        val result = WeekMarkCalculator.calculate(date)
        assertTrue(result.isCalendarYearDifferentFromWeekYear)
        assertEquals(2025, result.calendarYear)
        assertEquals(2026, result.weekYear)
    }

    // 17. Week 1
    @Test
    fun testWeek1() {
        val date = LocalDate.of(2026, 1, 1) // Thursday
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(1, result.weekNumber)
        assertEquals(2026, result.weekYear)
        assertTrue(result.isFirstWeekOfWeekYear)
    }

    // 18. Week 52
    @Test
    fun testWeek52() {
        val date = LocalDate.of(2025, 12, 28) // Sunday
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(52, result.weekNumber)
        assertEquals(2025, result.weekYear)
        assertTrue(result.isLastWeekOfWeekYear)
    }

    // 19. Week 53
    @Test
    fun testWeek53() {
        // 2020 was a 53-week year
        val date = LocalDate.of(2020, 12, 31)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(53, result.weekNumber)
        assertEquals(2020, result.weekYear)
        assertTrue(result.isLastWeekOfWeekYear)
    }

    // 20. First ISO week flag
    @Test
    fun testFirstIsoWeekFlag() {
        val week1Date = LocalDate.of(2026, 1, 4)
        val result1 = WeekMarkCalculator.calculate(week1Date)
        assertTrue(result1.isFirstWeekOfWeekYear)

        val week2Date = LocalDate.of(2026, 1, 5)
        val result2 = WeekMarkCalculator.calculate(week2Date)
        assertFalse(result2.isFirstWeekOfWeekYear)
    }

    // 21. Last ISO week flag
    @Test
    fun testLastIsoWeekFlag() {
        val lastWeekDate2020 = LocalDate.of(2020, 12, 28)
        val result2020 = WeekMarkCalculator.calculate(lastWeekDate2020)
        assertTrue(result2020.isLastWeekOfWeekYear)

        val midWeekDate = LocalDate.of(2020, 6, 15)
        val resultMid = WeekMarkCalculator.calculate(midWeekDate)
        assertFalse(resultMid.isLastWeekOfWeekYear)
    }

    // 22. 52-week ISO year
    @Test
    fun test52WeekIsoYear() {
        val weeks2025 = WeekMarkCalculator.calculateWeeksInIsoYear(2025)
        assertEquals(52, weeks2025)

        val weeks2024 = WeekMarkCalculator.calculateWeeksInIsoYear(2024)
        assertEquals(52, weeks2024)
    }

    // 23. 53-week ISO year
    @Test
    fun test53WeekIsoYear() {
        val weeks2020 = WeekMarkCalculator.calculateWeeksInIsoYear(2020)
        assertEquals(53, weeks2020)

        val weeks2026 = WeekMarkCalculator.calculateWeeksInIsoYear(2026)
        assertEquals(53, weeks2026)

        val weeks2015 = WeekMarkCalculator.calculateWeeksInIsoYear(2015)
        assertEquals(53, weeks2015)
    }

    // 24. Year boundary crossing
    @Test
    fun testYearBoundaryCrossing() {
        // Week 53 of 2026 starts on Monday Dec 28, 2026 and ends on Sunday Jan 3, 2027
        val date = LocalDate.of(2026, 12, 30)
        val result = WeekMarkCalculator.calculate(date)
        assertEquals(LocalDate.of(2026, 12, 28), result.weekStart)
        assertEquals(LocalDate.of(2027, 1, 3), result.weekEnd)
        assertEquals(53, result.weekNumber)
        assertEquals(2026, result.weekYear)
    }

    // 25. December dates
    @Test
    fun testDecemberDates() {
        val dec25 = LocalDate.of(2026, 12, 25) // Friday
        val result = WeekMarkCalculator.calculate(dec25)
        assertEquals(52, result.weekNumber)
        assertEquals(2026, result.weekYear)
    }

    // 26. January dates
    @Test
    fun testJanuaryDates() {
        // Jan 1, 2021 was Friday -> belongs to Week 53 of ISO year 2020!
        val jan1 = LocalDate.of(2021, 1, 1)
        val result = WeekMarkCalculator.calculate(jan1)
        assertEquals(53, result.weekNumber)
        assertEquals(2020, result.weekYear)
        assertEquals(2021, result.calendarYear)
        assertTrue(result.isCalendarYearDifferentFromWeekYear)
    }

    // 27. Leap year
    @Test
    fun testLeapYear() {
        val leapYearDate = LocalDate.of(2024, 7, 15)
        val result = WeekMarkCalculator.calculate(leapYearDate)
        assertEquals(29, result.weekNumber)
        assertEquals(2024, result.weekYear)
    }

    // 28. February 29
    @Test
    fun testFebruary29() {
        val leapDay = LocalDate.of(2024, 2, 29) // Thursday
        val result = WeekMarkCalculator.calculate(leapDay)
        assertEquals(9, result.weekNumber)
        assertEquals(2024, result.weekYear)
        assertEquals(LocalDate.of(2024, 2, 26), result.weekStart)
        assertEquals(LocalDate.of(2024, 3, 3), result.weekEnd)
    }

    // 29. Previous week
    @Test
    fun testPreviousWeek() {
        val date = LocalDate.of(2026, 10, 14)
        val prev = WeekMarkCalculator.previousWeek(date)
        assertEquals(LocalDate.of(2026, 10, 7), prev)
    }

    // 30. Next week
    @Test
    fun testNextWeek() {
        val date = LocalDate.of(2026, 10, 14)
        val next = WeekMarkCalculator.nextWeek(date)
        assertEquals(LocalDate.of(2026, 10, 21), next)
    }

    // 31. Previous day
    @Test
    fun testPreviousDay() {
        val date = LocalDate.of(2026, 10, 14)
        val prev = WeekMarkCalculator.previousDay(date)
        assertEquals(LocalDate.of(2026, 10, 13), prev)
    }

    // 32. Next day
    @Test
    fun testNextDay() {
        val date = LocalDate.of(2026, 10, 14)
        val next = WeekMarkCalculator.nextDay(date)
        assertEquals(LocalDate.of(2026, 10, 15), next)
    }

    // 33. Today
    @Test
    fun testTodayCalculation() {
        val today = LocalDate.now()
        val result = WeekMarkCalculator.calculate(today)
        assertEquals(today, result.selectedDate)
        assertTrue(result.weekNumber in 1..53)
    }

    // 34. Start of month
    @Test
    fun testStartOfMonth() {
        val date = LocalDate.of(2026, 10, 14)
        val start = WeekMarkCalculator.startOfMonth(date)
        assertEquals(LocalDate.of(2026, 10, 1), start)
    }

    // 35. End of month
    @Test
    fun testEndOfMonth() {
        val date = LocalDate.of(2026, 10, 14)
        val end = WeekMarkCalculator.endOfMonth(date)
        assertEquals(LocalDate.of(2026, 10, 31), end)

        val febLeap = LocalDate.of(2024, 2, 10)
        assertEquals(LocalDate.of(2024, 2, 29), WeekMarkCalculator.endOfMonth(febLeap))
    }

    // 36. Start of year
    @Test
    fun testStartOfYear() {
        val date = LocalDate.of(2026, 10, 14)
        val start = WeekMarkCalculator.startOfYear(date)
        assertEquals(LocalDate.of(2026, 1, 1), start)
    }

    // 37. End of year
    @Test
    fun testEndOfYear() {
        val date = LocalDate.of(2026, 10, 14)
        val end = WeekMarkCalculator.endOfYear(date)
        assertEquals(LocalDate.of(2026, 12, 31), end)
    }

    // CRITICAL ISO BOUNDARY TESTS
    @Test
    fun testCriticalIsoDatesAroundYearBoundary() {
        // 29 Dec 2025 is Monday -> Week 1 of ISO 2026
        val d29Dec2025 = LocalDate.of(2025, 12, 29)
        val res29Dec = WeekMarkCalculator.calculate(d29Dec2025)
        assertEquals(1, res29Dec.weekNumber)
        assertEquals(2026, res29Dec.weekYear)
        assertEquals(2025, res29Dec.calendarYear)
        assertTrue(res29Dec.isCalendarYearDifferentFromWeekYear)

        // 30 Dec 2025 is Tuesday -> Week 1 of ISO 2026
        val d30Dec2025 = LocalDate.of(2025, 12, 30)
        val res30Dec = WeekMarkCalculator.calculate(d30Dec2025)
        assertEquals(1, res30Dec.weekNumber)
        assertEquals(2026, res30Dec.weekYear)

        // 31 Dec 2025 is Wednesday -> Week 1 of ISO 2026
        val d31Dec2025 = LocalDate.of(2025, 12, 31)
        val res31Dec = WeekMarkCalculator.calculate(d31Dec2025)
        assertEquals(1, res31Dec.weekNumber)
        assertEquals(2026, res31Dec.weekYear)

        // 1 Jan 2026 is Thursday -> Week 1 of ISO 2026
        val d1Jan2026 = LocalDate.of(2026, 1, 1)
        val res1Jan = WeekMarkCalculator.calculate(d1Jan2026)
        assertEquals(1, res1Jan.weekNumber)
        assertEquals(2026, res1Jan.weekYear)
        assertEquals(2026, res1Jan.calendarYear)
        assertFalse(res1Jan.isCalendarYearDifferentFromWeekYear)
        assertEquals(LocalDate.of(2025, 12, 29), res1Jan.weekStart)
        assertEquals(LocalDate.of(2026, 1, 4), res1Jan.weekEnd)

        // 2 Jan 2026 is Friday -> Week 1 of ISO 2026
        val d2Jan2026 = LocalDate.of(2026, 1, 2)
        val res2Jan = WeekMarkCalculator.calculate(d2Jan2026)
        assertEquals(1, res2Jan.weekNumber)
        assertEquals(2026, res2Jan.weekYear)

        // 3 Jan 2026 is Saturday -> Week 1 of ISO 2026
        val d3Jan2026 = LocalDate.of(2026, 1, 3)
        val res3Jan = WeekMarkCalculator.calculate(d3Jan2026)
        assertEquals(1, res3Jan.weekNumber)
        assertEquals(2026, res3Jan.weekYear)

        // 4 Jan 2026 is Sunday -> Week 1 of ISO 2026
        val d4Jan2026 = LocalDate.of(2026, 1, 4)
        val res4Jan = WeekMarkCalculator.calculate(d4Jan2026)
        assertEquals(1, res4Jan.weekNumber)
        assertEquals(2026, res4Jan.weekYear)

        // 5 Jan 2026 is Monday -> Week 2 of ISO 2026
        val d5Jan2026 = LocalDate.of(2026, 1, 5)
        val res5Jan = WeekMarkCalculator.calculate(d5Jan2026)
        assertEquals(2, res5Jan.weekNumber)
        assertEquals(2026, res5Jan.weekYear)
        assertEquals(LocalDate.of(2026, 1, 5), res5Jan.weekStart)
        assertEquals(LocalDate.of(2026, 1, 11), res5Jan.weekEnd)
    }
}
