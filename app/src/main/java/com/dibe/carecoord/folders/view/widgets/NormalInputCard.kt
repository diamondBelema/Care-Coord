package com.dibe.carecoord.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.dibe.carecoord.folders.view.widgets.Header
import java.lang.Error

@Composable
fun NormalInputCard(
        header: String,
        value: String,
        onValueChange: (String) -> Unit,
        trailingIcon: String? = null,
        maxLines: Int = 1,
        isError: Boolean = false,
        errorMessage: String? = ""
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
            Header(header)
            TextField(
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true
                                                 ),
                trailingIcon = trailingIcon?.let { { Text(it) } },
                maxLines = maxLines,
                isError = isError,
                modifier = Modifier.fillMaxWidth()
                     )
            if (isError && !errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                    )
            }
        }
    }
}
