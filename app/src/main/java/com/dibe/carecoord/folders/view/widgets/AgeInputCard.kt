package com.dibe.carecoord.folders.view.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dibe.carecoord.folders.viewmodel.FolderState

@Composable
fun CustomAgeInputCard(
        state: FolderState,
        onValueChange: (Int) -> Unit,
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
               ) {
                Text(
                    text = "Age:",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(80.dp)
                    )
                Text(
                    text = state.patient.age.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                    )
            }

            var age by remember { mutableIntStateOf(0) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                Text(
                    text = "Scroll to Select Age",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                    )

                LazyColumn(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                          ) {
                    items(101) { index ->
                        val distanceFromCenter = kotlin.math.abs(index - age)
                        val fontSize = when {
                            distanceFromCenter == 0 -> 32.sp // Selected item at the center
                            distanceFromCenter == 1 -> 28.sp // Items adjacent to the center
                            else -> 24.sp // Other items
                        }

                        Text(
                            text = index.toString(),
                            fontSize = fontSize,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    age = index
                                    onValueChange(age)
                                }
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                    }
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
}
