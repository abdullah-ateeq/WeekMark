package com.example.weekmark.data.repository

import com.example.weekmark.domain.WeekMarkCalculator
import com.example.weekmark.testutil.FakeSharedPreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class WeekMarkHistoryRepositoryTest {

    private lateinit var fakePrefs: FakeSharedPreferences
    private lateinit var repository: WeekMarkHistoryRepository

    @Before
    fun setUp() {
        fakePrefs = FakeSharedPreferences()
        repository = WeekMarkHistoryRepository(fakePrefs)
    }

    @Test
    fun testSaveAndLoadHistory() {
        val date1 = LocalDate.of(2026, 10, 14)
        val res1 = WeekMarkCalculator.calculate(date1)
        repository.saveCalculation(res1)

        val loaded = repository.history.value
        assertEquals(1, loaded.size)
        assertEquals(date1, loaded[0].selectedDate)
        assertEquals(42, loaded[0].weekNumber)
        assertEquals(2026, loaded[0].weekYear)
    }

    @Test
    fun testHistoryLimitMax10() {
        // Add 12 different dates
        for (i in 1..12) {
            val date = LocalDate.of(2026, 1, i)
            val result = WeekMarkCalculator.calculate(date)
            repository.saveCalculation(result, currentTimeMillis = i * 1000L)
        }

        val history = repository.history.value
        assertEquals(10, history.size)
        // Most recent should be Jan 12, oldest kept should be Jan 3 (Jan 1 and Jan 2 were dropped)
        assertEquals(LocalDate.of(2026, 1, 12), history[0].selectedDate)
        assertEquals(LocalDate.of(2026, 1, 3), history[9].selectedDate)
    }

    @Test
    fun testDeduplicationOnSameDate() {
        val date = LocalDate.of(2026, 10, 14)
        val result = WeekMarkCalculator.calculate(date)

        repository.saveCalculation(result, currentTimeMillis = 1000L)
        assertEquals(1, repository.history.value.size)

        // Save again for the same date
        repository.saveCalculation(result, currentTimeMillis = 2000L)
        assertEquals(1, repository.history.value.size)
        assertEquals(2000L, repository.history.value[0].calculatedAt)
    }

    @Test
    fun testDeleteCalculation() {
        val d1 = LocalDate.of(2026, 10, 14)
        val d2 = LocalDate.of(2026, 10, 15)

        val entry1 = repository.saveCalculation(WeekMarkCalculator.calculate(d1))
        repository.saveCalculation(WeekMarkCalculator.calculate(d2))

        assertEquals(2, repository.history.value.size)

        repository.deleteCalculation(entry1.id)
        assertEquals(1, repository.history.value.size)
        assertEquals(d2, repository.history.value[0].selectedDate)
    }

    @Test
    fun testClearHistory() {
        repository.saveCalculation(WeekMarkCalculator.calculate(LocalDate.of(2026, 10, 14)))
        repository.saveCalculation(WeekMarkCalculator.calculate(LocalDate.of(2026, 10, 15)))

        assertEquals(2, repository.history.value.size)

        repository.clearHistory()
        assertTrue(repository.history.value.isEmpty())

        // Recreating repository from same preferences should still be empty
        val reloadedRepo = WeekMarkHistoryRepository(fakePrefs)
        assertTrue(reloadedRepo.history.value.isEmpty())
    }
}
