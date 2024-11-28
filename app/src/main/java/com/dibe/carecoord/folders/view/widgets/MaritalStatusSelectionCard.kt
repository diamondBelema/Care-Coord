package com.dibe.carecoord.folders.view.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState

@Composable
fun MaritalStatusSelectionCard(
        header: String,
        state: FolderState,
        onValueChange: (String) -> Unit,
        isError: Boolean = false,
        errorMessage: String? = null
                              ) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
              ) {
            Text(
                text = header,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
                )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
               ) {
                MaritalStatusOption(
                    text = "M",
                    state = state,
                    isSelected = state.patient.maritalStatus == "M",
                    onValueChange = onValueChange
                                   )
                MaritalStatusOption(
                    text = "S",
                    state = state,
                    isSelected = state.patient.maritalStatus == "S",
                    onValueChange = onValueChange
                                   )
                MaritalStatusOption(
                    text = "D",
                    state = state,
                    isSelected = state.patient.maritalStatus == "D",
                    onValueChange = onValueChange
                                   )
            }
            if (isError && !errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                    )
            }
        }
    }
}
