package com.example.weekmark.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.weekmark.data.model.ThemeMode
import com.example.weekmark.data.model.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(
    private val prefs: SharedPreferences
) {
    constructor(context: Context) : this(
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    )

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    private fun loadSettings(): UserSettings {
        val themeOrdinal = prefs.getInt(KEY_THEME_MODE, ThemeMode.SYSTEM.ordinal)
        val themeMode = ThemeMode.entries.getOrElse(themeOrdinal) { ThemeMode.SYSTEM }
        val dynamicColor = prefs.getBoolean(KEY_DYNAMIC_COLOR, true)
        val hapticFeedback = prefs.getBoolean(KEY_HAPTIC_FEEDBACK, true)
        val animations = prefs.getBoolean(KEY_ANIMATIONS, true)
        val confirmHistoryDelete = prefs.getBoolean(KEY_CONFIRM_HISTORY_DELETE, true)
        val showIsoDetails = prefs.getBoolean(KEY_SHOW_ISO_DETAILS, true)
        val preserveLastDate = prefs.getBoolean(KEY_PRESERVE_LAST_DATE, true)
        val lastSelectedDate = prefs.getString(KEY_LAST_SELECTED_DATE, null)

        return UserSettings(
            themeMode = themeMode,
            dynamicColor = dynamicColor,
            hapticFeedback = hapticFeedback,
            animations = animations,
            confirmHistoryDelete = confirmHistoryDelete,
            showIsoDetails = showIsoDetails,
            preserveLastDate = preserveLastDate,
            lastSelectedDate = lastSelectedDate
        )
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putInt(KEY_THEME_MODE, mode.ordinal).apply()
        _settings.value = _settings.value.copy(themeMode = mode)
    }

    fun setDynamicColor(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DYNAMIC_COLOR, enabled).apply()
        _settings.value = _settings.value.copy(dynamicColor = enabled)
    }

    fun setHapticFeedback(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTIC_FEEDBACK, enabled).apply()
        _settings.value = _settings.value.copy(hapticFeedback = enabled)
    }

    fun setAnimations(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ANIMATIONS, enabled).apply()
        _settings.value = _settings.value.copy(animations = enabled)
    }

    fun setConfirmHistoryDelete(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_CONFIRM_HISTORY_DELETE, enabled).apply()
        _settings.value = _settings.value.copy(confirmHistoryDelete = enabled)
    }

    fun setShowIsoDetails(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_ISO_DETAILS, enabled).apply()
        _settings.value = _settings.value.copy(showIsoDetails = enabled)
    }

    fun setPreserveLastDate(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PRESERVE_LAST_DATE, enabled).apply()
        _settings.value = _settings.value.copy(preserveLastDate = enabled)
    }

    fun setLastSelectedDate(dateIso: String?) {
        prefs.edit().putString(KEY_LAST_SELECTED_DATE, dateIso).apply()
        _settings.value = _settings.value.copy(lastSelectedDate = dateIso)
    }

    fun resetSettings() {
        prefs.edit().clear().apply()
        val defaultSettings = UserSettings()
        _settings.value = defaultSettings
    }

    companion object {
        private const val PREFS_NAME = "weekmark_settings"
        private const val KEY_THEME_MODE = "key_theme_mode"
        private const val KEY_DYNAMIC_COLOR = "key_dynamic_color"
        private const val KEY_HAPTIC_FEEDBACK = "key_haptic_feedback"
        private const val KEY_ANIMATIONS = "key_animations"
        private const val KEY_CONFIRM_HISTORY_DELETE = "key_confirm_history_delete"
        private const val KEY_SHOW_ISO_DETAILS = "key_show_iso_details"
        private const val KEY_PRESERVE_LAST_DATE = "key_preserve_last_date"
        private const val KEY_LAST_SELECTED_DATE = "key_last_selected_date"
    }
}
