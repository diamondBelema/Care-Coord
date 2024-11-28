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
fun BloodPressureCard(sysValue: String, onSysChange: (String) -> Unit, dysValue: String, onDysChange: (String) -> Unit, isEdit: Boolean = false) {
    Card(
        modifier = if (isEdit) Modifier.padding(5.dp) else Modifier
            .padding(15.dp)
            .fillMaxWidth()
        ) {
        Column (
            modifier = if (isEdit) Modifier else Modifier.padding(20.dp)
               ){
            if (isEdit) Text(text = "Blood Pressure:", fontSize = 16.sp) else Header("Blood Pressure:")
            Row(
                horizontalArrangement = Arrangement.Center ,
                modifier = Modifier.fillMaxWidth()
               ) {
                TextField(
                    modifier = if (isEdit) Modifier.weight(1f).padding(end = 5.dp) else Modifier.padding(10.dp).weight(1f),
                    value = if (sysValue == "0") "" else sysValue ,
                    onValueChange = {
                        if (it == "") {
                            onSysChange("0")
                        } else {
                            onSysChange(it)
                        }
                    } ,
                    label = { Text(text = "SYS", fontSize = 15.sp) } ,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) ,
                    maxLines = 1
                         )
                TextField(
                    modifier = if (isEdit) Modifier.weight(1f).padding(start = 5.dp) else Modifier.padding(10.dp).weight(1f) ,
                    value = if (dysValue == "0") "" else dysValue ,
                    onValueChange =  {
                        if (it == "") {
                            onDysChange("0")
                        } else {
                            onDysChange(it)
                        }
                    } ,
                    label = { Text(text = "DYS", fontSize = 15.sp) } ,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) ,
                    maxLines = 1
                         )
            }
        }
    }
}