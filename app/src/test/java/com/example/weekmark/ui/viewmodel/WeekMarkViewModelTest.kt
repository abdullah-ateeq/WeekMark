package com.example.weekmark.ui.viewmodel

import com.example.weekmark.data.model.CalculationState
import com.example.weekmark.data.model.ThemeMode
import com.example.weekmark.data.model.WeekMarkHistory
import com.example.weekmark.data.repository.SettingsRepository
import com.example.weekmark.data.repository.WeekMarkHistoryRepository
import com.example.weekmark.domain.WeekMarkCalculator
import com.example.weekmark.testutil.FakeSharedPreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class WeekMarkViewModelTest {

    private lateinit var fakePrefs: FakeSharedPreferences
    private lateinit var historyRepository: WeekMarkHistoryRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: WeekMarkViewModel

    @Before
    fun setUp() {
        fakePrefs = FakeSharedPreferences()
        historyRepository = WeekMarkHistoryRepository(fakePrefs)
        settingsRepository = SettingsRepository(fakePrefs)
        viewModel = WeekMarkViewModel(historyRepository, settingsRepository)
    }

    @Test
    fun testInitialStateHasResult() {
        val state = viewModel.uiState.value
        assertNotNull(state.result)
        assertEquals(CalculationState.Calculated, state.calculationState)
        assertEquals(LocalDate.now(), state.selectedDate)
    }

    @Test
    fun testPreserveLastDateRestoration() {
        // Prepare preferences with a stored date
        val prefsWithStoredDate = FakeSharedPreferences()
        prefsWithStoredDate.edit()
            .putBoolean("key_preserve_last_date", true)
            .putString("key_last_selected_date", "2026-10-14")
            .apply()

        val customSettingsRepo = SettingsRepository(prefsWithStoredDate)
        val customHistoryRepo = WeekMarkHistoryRepository(prefsWithStoredDate)
        val restoredViewModel = WeekMarkViewModel(customHistoryRepo, customSettingsRepo)

        val state = restoredViewModel.uiState.value
        assertEquals(LocalDate.of(2026, 10, 14), state.selectedDate)
        assertEquals(42, state.result?.weekNumber)
    }

    @Test
    fun testSelectDateUpdatesResult() {
        val targetDate = LocalDate.of(2026, 1, 1)
        viewModel.selectDate(targetDate)

        val state = viewModel.uiState.value
        assertEquals(targetDate, state.selectedDate)
        assertEquals(1, state.result?.weekNumber)
        assertEquals(2026, state.result?.weekYear)
    }

    @Test
    fun testSelectToday() {
        viewModel.selectDate(LocalDate.of(2020, 1, 1))
        viewModel.selectToday()

        assertEquals(LocalDate.now(), viewModel.uiState.value.selectedDate)
    }

    @Test
    fun testNavigationControls() {
        val start = LocalDate.of(2026, 10, 14)
        viewModel.selectDate(start)

        viewModel.nextWeek()
        assertEquals(LocalDate.of(2026, 10, 21), viewModel.uiState.value.selectedDate)

        viewModel.previousWeek()
        assertEquals(LocalDate.of(2026, 10, 14), viewModel.uiState.value.selectedDate)

        viewModel.nextDay()
        assertEquals(LocalDate.of(2026, 10, 15), viewModel.uiState.value.selectedDate)

        viewModel.previousDay()
        assertEquals(LocalDate.of(2026, 10, 14), viewModel.uiState.value.selectedDate)
    }

    @Test
    fun testMonthAndYearShortcuts() {
        val start = LocalDate.of(2026, 10, 14)
        viewModel.selectDate(start)

        viewModel.startOfMonth()
        assertEquals(LocalDate.of(2026, 10, 1), viewModel.uiState.value.selectedDate)

        viewModel.endOfMonth()
        assertEquals(LocalDate.of(2026, 10, 31), viewModel.uiState.value.selectedDate)

        viewModel.startOfYear()
        assertEquals(LocalDate.of(2026, 1, 1), viewModel.uiState.value.selectedDate)

        viewModel.endOfYear()
        assertEquals(LocalDate.of(2026, 12, 31), viewModel.uiState.value.selectedDate)
    }

    @Test
    fun testCalculateWeekSavesHistory() {
        viewModel.selectDate(LocalDate.of(2026, 10, 14))
        viewModel.calculateWeek()

        assertEquals(1, historyRepository.history.value.size)
        assertEquals(LocalDate.of(2026, 10, 14), historyRepository.history.value[0].selectedDate)
    }

    @Test
    fun testRapidCalculationProtection() {
        viewModel.selectDate(LocalDate.of(2026, 10, 14))

        // First explicit calculate
        viewModel.calculateWeek()
        assertEquals(1, historyRepository.history.value.size)

        // Rapid immediate tap (within 300ms) on the same date should be ignored
        viewModel.calculateWeek()
        viewModel.calculateWeek()
        assertEquals(1, historyRepository.history.value.size)
    }

    @Test
    fun testResetToToday() {
        viewModel.selectDate(LocalDate.of(2025, 1, 1))
        viewModel.resetToToday()

        assertEquals(LocalDate.now(), viewModel.uiState.value.selectedDate)
    }

    @Test
    fun testLoadFromHistory() {
        val historyEntry = WeekMarkHistory(
            id = "test-id",
            selectedDate = LocalDate.of(2026, 5, 20),
            weekNumber = 21,
            weekYear = 2026,
            weekStart = LocalDate.of(2026, 5, 18),
            weekEnd = LocalDate.of(2026, 5, 24),
            calculatedAt = 1000L
        )

        viewModel.loadFromHistory(historyEntry)
        assertEquals(LocalDate.of(2026, 5, 20), viewModel.uiState.value.selectedDate)
        assertEquals(21, viewModel.uiState.value.result?.weekNumber)
    }

    @Test
    fun testDeleteAndClearHistory() {
        viewModel.selectDate(LocalDate.of(2026, 10, 14))
        viewModel.calculateWeek()

        assertEquals(1, historyRepository.history.value.size)
        val id = historyRepository.history.value[0].id

        viewModel.deleteHistoryItem(id)
        assertTrue(historyRepository.history.value.isEmpty())

        viewModel.selectDate(LocalDate.of(2026, 10, 15))
        viewModel.calculateWeek()
        assertFalse(historyRepository.history.value.isEmpty())

        viewModel.clearAllHistory()
        assertTrue(historyRepository.history.value.isEmpty())
    }

    @Test
    fun testSettingsToggles() {
        viewModel.setThemeMode(ThemeMode.DARK)
        assertEquals(ThemeMode.DARK, viewModel.userSettings.value.themeMode)

        viewModel.setShowIsoDetails(false)
        assertFalse(viewModel.userSettings.value.showIsoDetails)

        viewModel.setHapticFeedback(false)
        assertFalse(viewModel.userSettings.value.hapticFeedback)

        viewModel.setAnimations(false)
        assertFalse(viewModel.userSettings.value.animations)

        viewModel.resetSettings()
        assertEquals(ThemeMode.SYSTEM, viewModel.userSettings.value.themeMode)
        assertTrue(viewModel.userSettings.value.showIsoDetails)
    }

    @Test
    fun testCopyAndShareText() {
        viewModel.selectDate(LocalDate.of(2026, 10, 14))
        val copyText = viewModel.getCopyText()
        assertTrue(copyText.contains("W42"))
        assertTrue(copyText.contains("Wednesday, 14 October 2026"))

        val shareText = viewModel.getShareText()
        assertTrue(shareText.contains("WeekMark"))
        assertTrue(shareText.contains("W42"))
    }
}
