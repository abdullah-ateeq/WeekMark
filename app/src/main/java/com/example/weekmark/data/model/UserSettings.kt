package com.example.weekmark.data.model

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class UserSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = true,
    val hapticFeedback: Boolean = true,
    val animations: Boolean = true,
    val confirmHistoryDelete: Boolean = true,
    val showIsoDetails: Boolean = true,
    val preserveLastDate: Boolean = true,
    val lastSelectedDate: String? = null
)
