package com.example.dailyexpensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.core.states.GroupType
import com.example.dailyexpensetracker.core.states.TransactionFilterState
import com.example.dailyexpensetracker.ui.theme.size12
import com.example.dailyexpensetracker.ui.theme.size4
import com.example.dailyexpensetracker.ui.theme.size6
import com.example.dailyexpensetracker.ui.theme.size8

@Composable
fun FilterControls(
    filterState: TransactionFilterState,
    onFilterChange: (TransactionFilterState) -> Unit
) {
    // Container row to hold filter UI elements
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = size8),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Inner row for the group type toggle label and segmented toggle control
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            "Group by:".ExpenseText() // Label text
            Spacer(modifier = Modifier.width(size8))
            // Segmented toggle to switch between different group types
            SegmentedToggle(
                current = filterState.groupType,
                onToggle = {
                    // When a segment is selected, update the filter state with new group type
                    onFilterChange(filterState.copy(groupType = it))
                }
            )
        }
    }
}

@Composable
fun SegmentedToggle(
    current: GroupType,
    onToggle: (GroupType) -> Unit
) {
    // Row container with rounded corners and background for toggle segments
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(size12))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(size4)
    ) {
        // Loop through all group types to create toggle segments dynamically
        GroupType.entries.forEach { type ->
            val isSelected = current == type // Check if this segment is currently selected

            // Display the segment label with clickable and background based on selection state
            type.name.lowercase().replaceFirstChar { it.uppercase() }.ExpenseText(
                modifier = Modifier
                    .clip(RoundedCornerShape(size8))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .padding(horizontal = size12, vertical = size6)
                    .clickable {
                        // When clicked, call onToggle with the selected group type
                        onToggle(type)
                    },
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
