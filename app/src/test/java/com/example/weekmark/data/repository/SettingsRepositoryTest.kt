package com.example.weekmark.data.repository

import com.example.weekmark.data.model.ThemeMode
import com.example.weekmark.testutil.FakeSharedPreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SettingsRepositoryTest {

    private lateinit var fakePrefs: FakeSharedPreferences
    private lateinit var repository: SettingsRepository

    @Before
    fun setUp() {
        fakePrefs = FakeSharedPreferences()
        repository = SettingsRepository(fakePrefs)
    }

    @Test
    fun testDefaultSettings() {
        val settings = repository.settings.value
        assertEquals(ThemeMode.SYSTEM, settings.themeMode)
        assertTrue(settings.dynamicColor)
        assertTrue(settings.hapticFeedback)
        assertTrue(settings.animations)
        assertTrue(settings.confirmHistoryDelete)
        assertTrue(settings.showIsoDetails)
        assertTrue(settings.preserveLastDate)
        assertNull(settings.lastSelectedDate)
    }

    @Test
    fun testThemePersistence() {
        repository.setThemeMode(ThemeMode.DARK)
        assertEquals(ThemeMode.DARK, repository.settings.value.themeMode)

        val reloaded = SettingsRepository(fakePrefs)
        assertEquals(ThemeMode.DARK, reloaded.settings.value.themeMode)

        repository.setThemeMode(ThemeMode.LIGHT)
        assertEquals(ThemeMode.LIGHT, repository.settings.value.themeMode)
    }

    @Test
    fun testDynamicColorPersistence() {
        repository.setDynamicColor(false)
        assertFalse(repository.settings.value.dynamicColor)

        val reloaded = SettingsRepository(fakePrefs)
        assertFalse(reloaded.settings.value.dynamicColor)
    }

    @Test
    fun testHapticFeedbackPersistence() {
        repository.setHapticFeedback(false)
        assertFalse(repository.settings.value.hapticFeedback)

        val reloaded = SettingsRepository(fakePrefs)
        assertFalse(reloaded.settings.value.hapticFeedback)
    }

    @Test
    fun testAnimationsPersistence() {
        repository.setAnimations(false)
        assertFalse(repository.settings.value.animations)

        val reloaded = SettingsRepository(fakePrefs)
        assertFalse(reloaded.settings.value.animations)
    }

    @Test
    fun testConfirmHistoryDeletePersistence() {
        repository.setConfirmHistoryDelete(false)
        assertFalse(repository.settings.value.confirmHistoryDelete)

        val reloaded = SettingsRepository(fakePrefs)
        assertFalse(reloaded.settings.value.confirmHistoryDelete)
    }

    @Test
    fun testShowIsoDetailsPersistence() {
        repository.setShowIsoDetails(false)
        assertFalse(repository.settings.value.showIsoDetails)

        val reloaded = SettingsRepository(fakePrefs)
        assertFalse(reloaded.settings.value.showIsoDetails)
    }

    @Test
    fun testPreserveLastDatePersistence() {
        repository.setPreserveLastDate(false)
        assertFalse(repository.settings.value.preserveLastDate)

        val reloaded = SettingsRepository(fakePrefs)
        assertFalse(reloaded.settings.value.preserveLastDate)
    }

    @Test
    fun testLastSelectedDatePersistence() {
        repository.setLastSelectedDate("2026-10-14")
        assertEquals("2026-10-14", repository.settings.value.lastSelectedDate)

        val reloaded = SettingsRepository(fakePrefs)
        assertEquals("2026-10-14", reloaded.settings.value.lastSelectedDate)
    }

    @Test
    fun testResetSettings() {
        repository.setThemeMode(ThemeMode.DARK)
        repository.setHapticFeedback(false)
        repository.setAnimations(false)
        repository.setLastSelectedDate("2026-10-14")

        repository.resetSettings()

        val settings = repository.settings.value
        assertEquals(ThemeMode.SYSTEM, settings.themeMode)
        assertTrue(settings.hapticFeedback)
        assertTrue(settings.animations)
        assertNull(settings.lastSelectedDate)
    }
}
