package com.example.weekmark.data.model

import java.time.LocalDate

data class WeekMarkUiState(
    val selectedDate: LocalDate,
    val result: WeekMarkResult?,
    val history: List<WeekMarkHistory>,
    val calculationState: CalculationState,
    val errorMessage: String?,
    val isCalculating: Boolean
)
