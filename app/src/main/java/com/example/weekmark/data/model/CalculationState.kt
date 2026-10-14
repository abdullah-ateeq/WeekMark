package com.example.weekmark.data.model

sealed interface CalculationState {
    data object Idle : CalculationState
    data object Calculated : CalculationState
    data class Error(val message: String) : CalculationState
}

data class CalculationError(
    val message: String
)
