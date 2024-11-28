package com.dibe.carecoord.folders.view.widgets

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.folders.viewmodel.util.PatientField
import java.util.Calendar

@Composable
fun LmpAndEddCard(onEvent: (FolderEvent) -> Unit , state: FolderState , context: Context) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
        Column(modifier = Modifier.padding(20.dp)) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val lmpDatePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker , year: Int , month: Int , dayOfMonth: Int ->
                    onEvent(FolderEvent.ChangeValue(PatientField.LMP , "$dayOfMonth/${month + 1}/$year"))
                },
                year,
                month,
                day
                                                      )

            val eddDatePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker , year: Int , month: Int , dayOfMonth: Int ->
                    onEvent(FolderEvent.ChangeValue(PatientField.EDD , "$dayOfMonth/${month + 1}/$year"))
                },
                year,
                month,
                day
                                                      )

            Header("LMP:")
            TextField(
                value = state.patient.lmp,
                readOnly = true,
                onValueChange = { },
                trailingIcon = {
                    IconButton(onClick = { lmpDatePickerDialog.show() }) {
                        Icon(Icons.Default.CalendarToday , contentDescription = "Calendar")
                    }
                },
                maxLines = 1
                     )
            Spacer(modifier = Modifier.height(20.dp))
            Header("EDD:")
            TextField(
                value = state.patient.edd,
                readOnly = true,
                onValueChange = { },
                trailingIcon = {
                    IconButton(onClick = { eddDatePickerDialog.show() }) {
                        Icon(Icons.Default.CalendarToday , contentDescription = "Calendar")
                    }
                },
                maxLines = 1
                     )
        }
    }
}