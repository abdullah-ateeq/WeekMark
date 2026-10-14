package com.example.weekmark.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weekmark.data.model.WeekMarkResult
import com.example.weekmark.domain.WeekMarkFormatter

@Composable
fun IsoDetailsCard(
    result: WeekMarkResult,
    modifier: Modifier = Modifier
) {
    val dayOfWeekName = result.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "ISO standard details: Week number ${result.weekNumber}, ISO week year ${result.weekYear}, Calendar year ${result.calendarYear}, Day of year ${result.dayOfYear}, Total weeks in ISO year ${result.weeksInIsoYear}."
            },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "ISO-8601 SPECIFICATION DETAILS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Row 1: Week Number & ISO Week Year
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailTile(
                    label = "WEEK NUMBER",
                    value = WeekMarkFormatter.formatWeekNumber(result.weekNumber),
                    modifier = Modifier.weight(1f)
                )
                DetailTile(
                    label = "ISO WEEK YEAR",
                    value = result.weekYear.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 2: Calendar Year & Day of Year
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailTile(
                    label = "CALENDAR YEAR",
                    value = result.calendarYear.toString(),
                    modifier = Modifier.weight(1f)
                )
                DetailTile(
                    label = "DAY OF YEAR",
                    value = result.dayOfYear.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 3: Weeks in ISO Year & Day of Week
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailTile(
                    label = "WEEKS IN ISO YEAR",
                    value = "${result.weeksInIsoYear} weeks",
                    modifier = Modifier.weight(1f)
                )
                DetailTile(
                    label = "DAY OF WEEK",
                    value = dayOfWeekName,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DetailTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
