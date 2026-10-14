package com.example.weekmark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.weekmark.ui.navigation.WeekMarkNavGraph
import com.example.weekmark.ui.theme.WeekMarkTheme
import com.example.weekmark.ui.viewmodel.WeekMarkViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WeekMarkViewModel by viewModels {
        WeekMarkViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userSettings by viewModel.userSettings.collectAsState()

            WeekMarkTheme(
                themeMode = userSettings.themeMode,
                dynamicColor = userSettings.dynamicColor
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WeekMarkNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}