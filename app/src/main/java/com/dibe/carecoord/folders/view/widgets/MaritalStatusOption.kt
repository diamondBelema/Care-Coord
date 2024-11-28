package com.dibe.carecoord.folders.view.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.folders.viewmodel.util.PatientField

@Composable
fun MaritalStatusOption(
        text: String,
        state: FolderState ,
        onValueChange: (String) -> Unit,
        isSelected: Boolean
                       ) {
    Box(
        modifier = Modifier
            .clickable { onValueChange(text) }
            .padding(8.dp)
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
            .border(1.dp , MaterialTheme.colorScheme.primary , shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp , vertical = 8.dp)
       ) {
        Text(
            text = text ,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant ,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
    }
}