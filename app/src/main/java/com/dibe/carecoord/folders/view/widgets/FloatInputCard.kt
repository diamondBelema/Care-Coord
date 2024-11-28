package com.dibe.carecoord.folders.view.widgets

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FloatInputCard(
        header: String,
        value: String,
        onValueChange: (Float) -> Unit,
        trailingIcon: String? = null
                  ) {
    // Split initial value into main and decimal parts
    var mainPart by remember { mutableStateOf(value.substringBefore(".")) }
    var decimalPart by remember { mutableStateOf(value.substringAfter(".")) }

    // Function to update the combined value based on changes in main or decimal part
    fun updateValue() {
        try {
            onValueChange("$mainPart.$decimalPart".toFloat())
        } catch (e: NumberFormatException){
            e.printStackTrace()
        }
    }

    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
        Column(
            modifier = Modifier.padding(20.dp)
              ) {
            Header(header)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
               ) {
                // TextField for the main part (integer part)
                TextField(
                    value = if (mainPart == "0") "" else mainPart,
                    onValueChange = {
                        mainPart = if (it == "") {
                            "0"
                        } else {
                            it
                        }
                        updateValue()
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                         )

                // Separator between main and decimal parts
                Text(
                    text = ".",
                    modifier = Modifier.padding(horizontal = 4.dp)
                    )

                // TextField for the decimal part
                TextField(
                    value = if (decimalPart == "0") "" else decimalPart,
                    onValueChange = {
                        decimalPart = if (it == "") {
                            "0"
                        } else {
                            it
                        }
                        updateValue()
                        Log.i("MyTag", "$mainPart.$decimalPart")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                         )

                // Optional trailing icon (e.g., units)
                trailingIcon?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(start = 8.dp)
                        )
                }
            }
        }
    }
}
