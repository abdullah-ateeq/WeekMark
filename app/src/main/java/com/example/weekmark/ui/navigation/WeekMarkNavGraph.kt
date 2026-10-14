package com.example.weekmark.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weekmark.ui.about.AboutScreen
import com.example.weekmark.ui.history.HistoryScreen
import com.example.weekmark.ui.home.HomeScreen
import com.example.weekmark.ui.settings.SettingsScreen
import com.example.weekmark.ui.splash.SplashScreen
import com.example.weekmark.ui.viewmodel.WeekMarkViewModel

@Composable
fun WeekMarkNavGraph(
    viewModel: WeekMarkViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                uiState = uiState,
                userSettings = userSettings,
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToAbout = { navController.navigate(Screen.About.route) }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                historyList = uiState.history,
                userSettings = userSettings,
                onSelectHistoryItem = { historyItem ->
                    viewModel.loadFromHistory(historyItem)
                    navController.popBackStack()
                },
                onDeleteItem = { id ->
                    viewModel.deleteHistoryItem(id)
                },
                onClearAll = {
                    viewModel.clearAllHistory()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                userSettings = userSettings,
                onThemeModeChange = { viewModel.setThemeMode(it) },
                onDynamicColorChange = { viewModel.setDynamicColor(it) },
                onHapticFeedbackChange = { viewModel.setHapticFeedback(it) },
                onAnimationsChange = { viewModel.setAnimations(it) },
                onConfirmHistoryDeleteChange = { viewModel.setConfirmHistoryDelete(it) },
                onShowIsoDetailsChange = { viewModel.setShowIsoDetails(it) },
                onPreserveLastDateChange = { viewModel.setPreserveLastDate(it) },
                onClearHistory = { viewModel.clearAllHistory() },
                onResetSettings = { viewModel.resetSettings() },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
