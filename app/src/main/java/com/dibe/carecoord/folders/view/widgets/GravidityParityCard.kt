package com.dibe.carecoord.folders.view.widgets

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GravidityParityCard(
        gravidity: String,
        parity: String,
        onValueChange: (String, String) -> Unit
                       ) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Header("Gravidity And Parity:")
            Row(
                horizontalArrangement = Arrangement.Center ,
                modifier = Modifier.fillMaxWidth()
               ) {
                TextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f) ,
                    value = if (gravidity == "0") "" else gravidity ,
                    placeholder = { Text(text = "Gravidity", fontSize = 15.sp) } ,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) ,
                    onValueChange = { newValue ->
                        if (newValue == "") {
                            onValueChange("0", parity)
                        } else {
                            onValueChange(newValue, parity)
                        }
                                    } ,
                    maxLines = 1
                         )
                TextField(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f) ,
                    value = if (parity == "0") "" else parity ,
                    placeholder = { Text(text = "Parity", fontSize = 15.sp) } ,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) ,
                    onValueChange = { newValue ->
                        if (newValue == "") {
                            onValueChange(gravidity, "0")
                        } else {
                            onValueChange(gravidity, newValue)
                        }

                                    } ,
                    maxLines = 1
                         )
            }
        }
    }
}
