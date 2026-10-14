package com.example.weekmark.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weekmark.data.model.UserSettings
import com.example.weekmark.data.model.WeekMarkUiState
import com.example.weekmark.ui.home.components.CopyShareBar
import com.example.weekmark.ui.home.components.DateCard
import com.example.weekmark.ui.home.components.DayPositionCard
import com.example.weekmark.ui.home.components.IsoDetailsCard
import com.example.weekmark.ui.home.components.QuickActionChips
import com.example.weekmark.ui.home.components.RecentCalculationsSection
import com.example.weekmark.ui.home.components.WeekBoundaryCard
import com.example.weekmark.ui.home.components.WeekMarkDatePickerDialog
import com.example.weekmark.ui.home.components.WeekNavigationControls
import com.example.weekmark.ui.home.components.WeekResultCard
import com.example.weekmark.ui.viewmodel.WeekMarkViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WeekMarkViewModel,
    uiState: WeekMarkUiState,
    userSettings: UserSettings,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDatePicker by remember { mutableStateOf(false) }

    fun performHaptic() {
        if (userSettings.hapticFeedback) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    if (showDatePicker) {
        WeekMarkDatePickerDialog(
            initialDate = uiState.selectedDate,
            onDateSelected = { newDate ->
                performHaptic()
                viewModel.selectDate(newDate, recordHistory = false)
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WeekMark",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            performHaptic()
                            onNavigateToHistory()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = "Open calculation history"
                        )
                    }
                    IconButton(
                        onClick = {
                            performHaptic()
                            onNavigateToSettings()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Open app settings"
                        )
                    }
                    IconButton(
                        onClick = {
                            performHaptic()
                            onNavigateToAbout()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "About WeekMark"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Subtitle
            Text(
                text = "Find the week around any date.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Date Card
            DateCard(
                selectedDate = uiState.selectedDate,
                onDateClick = {
                    performHaptic()
                    showDatePicker = true
                }
            )

            // Quick Date Shortcuts
            QuickActionChips(
                onToday = {
                    performHaptic()
                    viewModel.selectToday()
                },
                onYesterday = {
                    performHaptic()
                    viewModel.selectDate(uiState.selectedDate.minusDays(1), recordHistory = false)
                },
                onTomorrow = {
                    performHaptic()
                    viewModel.selectDate(uiState.selectedDate.plusDays(1), recordHistory = false)
                },
                onStartOfMonth = {
                    performHaptic()
                    viewModel.startOfMonth()
                },
                onEndOfMonth = {
                    performHaptic()
                    viewModel.endOfMonth()
                },
                onStartOfYear = {
                    performHaptic()
                    viewModel.startOfYear()
                },
                onEndOfYear = {
                    performHaptic()
                    viewModel.endOfYear()
                }
            )

            // Week & Day Navigation + Calculate Action
            WeekNavigationControls(
                onPreviousWeek = {
                    performHaptic()
                    viewModel.previousWeek()
                },
                onNextWeek = {
                    performHaptic()
                    viewModel.nextWeek()
                },
                onPreviousDay = {
                    performHaptic()
                    viewModel.previousDay()
                },
                onNextDay = {
                    performHaptic()
                    viewModel.nextDay()
                },
                onCalculate = {
                    performHaptic()
                    viewModel.calculateWeek()
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Calculated & saved to history")
                    }
                },
                onReset = {
                    performHaptic()
                    viewModel.resetToToday()
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Reset to today")
                    }
                }
            )

            // Main Week Result Display
            uiState.result?.let { result ->
                WeekResultCard(
                    result = result,
                    animationsEnabled = userSettings.animations
                )

                WeekBoundaryCard(result = result)

                DayPositionCard(result = result)

                if (userSettings.showIsoDetails) {
                    IsoDetailsCard(result = result)
                }

                CopyShareBar(
                    copyText = viewModel.getCopyText(),
                    shareText = viewModel.getShareText(),
                    onCopied = {
                        performHaptic()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Result copied to clipboard")
                        }
                    }
                )
            }

            // Recent Calculations Section
            RecentCalculationsSection(
                historyList = uiState.history,
                onHistoryItemClick = { historyItem ->
                    performHaptic()
                    viewModel.loadFromHistory(historyItem)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Loaded ${historyItem.selectedDate}")
                    }
                },
                onViewAllClick = {
                    performHaptic()
                    onNavigateToHistory()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
