package com.example.weekmark.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weekmark.data.model.CalculationState
import com.example.weekmark.data.model.ThemeMode
import com.example.weekmark.data.model.UserSettings
import com.example.weekmark.data.model.WeekMarkHistory
import com.example.weekmark.data.model.WeekMarkResult
import com.example.weekmark.data.model.WeekMarkUiState
import com.example.weekmark.data.repository.SettingsRepository
import com.example.weekmark.data.repository.WeekMarkHistoryRepository
import com.example.weekmark.domain.WeekMarkCalculator
import com.example.weekmark.domain.WeekMarkFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class WeekMarkViewModel(
    private val historyRepository: WeekMarkHistoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    constructor(application: Application) : this(
        historyRepository = WeekMarkHistoryRepository(application),
        settingsRepository = SettingsRepository(application)
    )

    private val initialDate: LocalDate = run {
        val initialSettings = settingsRepository.settings.value
        if (initialSettings.preserveLastDate && !initialSettings.lastSelectedDate.isNullOrBlank()) {
            try {
                LocalDate.parse(initialSettings.lastSelectedDate)
            } catch (_: Exception) {
                LocalDate.now()
            }
        } else {
            LocalDate.now()
        }
    }

    private val _uiState = MutableStateFlow(
        WeekMarkUiState(
            selectedDate = initialDate,
            result = WeekMarkCalculator.calculate(initialDate),
            history = historyRepository.history.value,
            calculationState = CalculationState.Calculated,
            errorMessage = null,
            isCalculating = false
        )
    )
    val uiState: StateFlow<WeekMarkUiState> = _uiState.asStateFlow()

    val userSettings: StateFlow<UserSettings> = settingsRepository.settings

    // Timestamp of the last explicit calculation to debounce rapid taps
    private var lastCalculatedTimestamp = 0L
    private var lastCalculatedDate: LocalDate? = null


    fun selectDate(date: LocalDate, recordHistory: Boolean = false) {
        val newResult = WeekMarkCalculator.calculate(date)
        _uiState.value = _uiState.value.copy(
            selectedDate = date,
            result = newResult,
            calculationState = CalculationState.Calculated,
            errorMessage = null
        )

        if (userSettings.value.preserveLastDate) {
            settingsRepository.setLastSelectedDate(date.toString())
        }

        if (recordHistory) {
            saveToHistoryInternal(newResult)
        }
    }

    fun calculateWeek() {
        val now = System.currentTimeMillis()
        val currentDate = _uiState.value.selectedDate

        // Rapid tap debouncing: prevent duplicate calculations if triggered within 300ms on the same date
        if (currentDate == lastCalculatedDate && (now - lastCalculatedTimestamp) < 300) {
            return
        }

        lastCalculatedTimestamp = now
        lastCalculatedDate = currentDate

        val calculatedResult = WeekMarkCalculator.calculate(currentDate)
        _uiState.value = _uiState.value.copy(
            selectedDate = currentDate,
            result = calculatedResult,
            calculationState = CalculationState.Calculated,
            errorMessage = null
        )

        if (userSettings.value.preserveLastDate) {
            settingsRepository.setLastSelectedDate(currentDate.toString())
        }

        saveToHistoryInternal(calculatedResult)
    }

    private fun saveToHistoryInternal(result: WeekMarkResult) {
        historyRepository.saveCalculation(result)
        // Also immediately update in-memory uiState history
        _uiState.value = _uiState.value.copy(history = historyRepository.history.value)
    }

    fun selectToday() {
        selectDate(LocalDate.now(), recordHistory = false)
    }

    fun previousWeek() {
        selectDate(WeekMarkCalculator.previousWeek(_uiState.value.selectedDate), recordHistory = false)
    }

    fun nextWeek() {
        selectDate(WeekMarkCalculator.nextWeek(_uiState.value.selectedDate), recordHistory = false)
    }

    fun previousDay() {
        selectDate(WeekMarkCalculator.previousDay(_uiState.value.selectedDate), recordHistory = false)
    }

    fun nextDay() {
        selectDate(WeekMarkCalculator.nextDay(_uiState.value.selectedDate), recordHistory = false)
    }

    fun startOfMonth() {
        selectDate(WeekMarkCalculator.startOfMonth(_uiState.value.selectedDate), recordHistory = false)
    }

    fun endOfMonth() {
        selectDate(WeekMarkCalculator.endOfMonth(_uiState.value.selectedDate), recordHistory = false)
    }

    fun startOfYear() {
        selectDate(WeekMarkCalculator.startOfYear(_uiState.value.selectedDate), recordHistory = false)
    }

    fun endOfYear() {
        selectDate(WeekMarkCalculator.endOfYear(_uiState.value.selectedDate), recordHistory = false)
    }

    fun loadFromHistory(history: WeekMarkHistory) {
        selectDate(history.selectedDate, recordHistory = false)
    }

    fun deleteHistoryItem(id: String) {
        historyRepository.deleteCalculation(id)
        _uiState.value = _uiState.value.copy(history = historyRepository.history.value)
    }

    fun clearAllHistory() {
        historyRepository.clearHistory()
        _uiState.value = _uiState.value.copy(history = emptyList())
    }

    fun resetToToday() {
        val today = LocalDate.now()
        val todayResult = WeekMarkCalculator.calculate(today)
        _uiState.value = _uiState.value.copy(
            selectedDate = today,
            result = todayResult,
            calculationState = CalculationState.Calculated,
            errorMessage = null
        )

        if (userSettings.value.preserveLastDate) {
            settingsRepository.setLastSelectedDate(today.toString())
        }
    }

    fun getCopyText(): String {
        val currentResult = _uiState.value.result ?: WeekMarkCalculator.calculate(_uiState.value.selectedDate)
        return WeekMarkFormatter.generateCopyText(currentResult)
    }

    fun getShareText(): String {
        val currentResult = _uiState.value.result ?: WeekMarkCalculator.calculate(_uiState.value.selectedDate)
        return WeekMarkFormatter.generateShareText(currentResult)
    }

    fun setThemeMode(mode: ThemeMode) {
        settingsRepository.setThemeMode(mode)
    }

    fun setDynamicColor(enabled: Boolean) {
        settingsRepository.setDynamicColor(enabled)
    }

    fun setHapticFeedback(enabled: Boolean) {
        settingsRepository.setHapticFeedback(enabled)
    }

    fun setAnimations(enabled: Boolean) {
        settingsRepository.setAnimations(enabled)
    }

    fun setConfirmHistoryDelete(enabled: Boolean) {
        settingsRepository.setConfirmHistoryDelete(enabled)
    }

    fun setShowIsoDetails(enabled: Boolean) {
        settingsRepository.setShowIsoDetails(enabled)
    }

    fun setPreserveLastDate(enabled: Boolean) {
        settingsRepository.setPreserveLastDate(enabled)
        if (enabled) {
            settingsRepository.setLastSelectedDate(_uiState.value.selectedDate.toString())
        } else {
            settingsRepository.setLastSelectedDate(null)
        }
    }

    fun resetSettings() {
        settingsRepository.resetSettings()
    }

    companion object {
        val Factory: androidx.lifecycle.ViewModelProvider.Factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(
                modelClass: Class<T>,
                extras: androidx.lifecycle.viewmodel.CreationExtras
            ): T {
                val application = checkNotNull(extras[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return WeekMarkViewModel(
                    historyRepository = WeekMarkHistoryRepository(application),
                    settingsRepository = SettingsRepository(application)
                ) as T
            }

            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                throw UnsupportedOperationException("CreationExtras required")
            }
        }
    }
}
