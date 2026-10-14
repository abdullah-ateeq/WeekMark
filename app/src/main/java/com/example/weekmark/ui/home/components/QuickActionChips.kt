package com.example.weekmark.ui.home.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun QuickActionChips(
    onToday: () -> Unit,
    onYesterday: () -> Unit,
    onTomorrow: () -> Unit,
    onStartOfMonth: () -> Unit,
    onEndOfMonth: () -> Unit,
    onStartOfYear: () -> Unit,
    onEndOfYear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(
            onClick = onToday,
            label = { Text("Today") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Today,
                    contentDescription = null,
                    modifier = Modifier.padding(2.dp)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                leadingIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier.semantics { contentDescription = "Quick action: Select today's date" }
        )

        AssistChip(
            onClick = onYesterday,
            label = { Text("Yesterday") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.semantics { contentDescription = "Quick action: Select yesterday's date" }
        )

        AssistChip(
            onClick = onTomorrow,
            label = { Text("Tomorrow") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.semantics { contentDescription = "Quick action: Select tomorrow's date" }
        )

        AssistChip(
            onClick = onStartOfMonth,
            label = { Text("Start of Month") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.semantics { contentDescription = "Quick action: Select first day of month" }
        )

        AssistChip(
            onClick = onEndOfMonth,
            label = { Text("End of Month") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.semantics { contentDescription = "Quick action: Select last day of month" }
        )

        AssistChip(
            onClick = onStartOfYear,
            label = { Text("Start of Year") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.semantics { contentDescription = "Quick action: Select first day of calendar year" }
        )

        AssistChip(
            onClick = onEndOfYear,
            label = { Text("End of Year") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.semantics { contentDescription = "Quick action: Select last day of calendar year" }
        )
    }
}
