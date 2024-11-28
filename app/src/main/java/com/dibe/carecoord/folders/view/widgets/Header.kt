package com.dibe.carecoord.folders.view.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(text: String) {
    Text(text = text, fontSize = 22.sp, modifier = Modifier.padding(bottom = 10.dp))
}