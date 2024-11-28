package com.dibe.carecoord.folders.view.widgets

import androidx.compose.foundation.layout.Column
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

@Composable
fun IntegerInputCard(header: String , value: String , onValueChange: (String) -> Unit , trailingIcon: String? = null) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
              ) {
            Header(header)
            TextField(
                value = if (value == "0") "" else value ,
                onValueChange = {
                    if (it == "") {
                        onValueChange("0")
                    } else {
                        onValueChange(it)
                    }
                } ,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) ,
                trailingIcon = trailingIcon?.let { { Text(it) } } ,
                maxLines = 1
                     )
        }
    }
}