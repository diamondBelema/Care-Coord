package com.dibe.carecoord.folders.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dibe.carecoord.R
import com.dibe.carecoord.Screens.NormalInputCard
import com.dibe.carecoord.folders.view.widgets.AlertDialog
import com.dibe.carecoord.folders.view.widgets.CustomAgeInputCard
import com.dibe.carecoord.folders.view.widgets.GenderSelectionCard
import com.dibe.carecoord.folders.view.widgets.MaritalStatusSelectionCard
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.folders.viewmodel.util.PatientField
import com.dibe.carecoord.old.Presentation.Screens.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDetails(onEvent: (FolderEvent) -> Unit, state: FolderState, navController: NavController) {
    var isOn by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    // Function to check if all required fields are filled
    fun validateFields(): Boolean {
        return state.patient.name.isNotBlank() &&
                state.patient.age != null &&
                state.patient.sex.isNotBlank() &&
                state.patient.occupation.isNotBlank() &&
                state.patient.maritalStatus.isNotBlank() &&
                state.patient.address.isNotBlank() &&
                state.patient.tribe.isNotBlank()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.basic_details_name)) },
                navigationIcon = {
                    IconButton(onClick = { isOn = true }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "the back")
                    }
                }
                     )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceEvenly
               ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f).padding(10.dp),
                    onClick = { isOn = true }
                              ) {
                    Text(text = "Home")
                }
                Button(
                    modifier = Modifier.weight(1f).padding(10.dp),
                    onClick = {
                        if (validateFields()) {
                            navController.navigate(Screens.BODY_PARAMETERS.name)
                        } else {
                            showError = true
                        }
                    }
                      ) {
                    Text(text = "Next")
                }
            }
        },
        snackbarHost = { SnackbarHost(state.snackbarHostState) }
            ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.ime)
              ) {

            NormalInputCard(
                header = "Name:",
                value = state.patient.name,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.NAME, newValue)) },
                isError = showError && state.patient.name.isBlank(),
                errorMessage = if (showError && state.patient.name.isBlank()) "This field is required" else null
                           )

            CustomAgeInputCard(
                state,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.AGE, newValue)) },
                isError = showError && state.patient.age == 0,
                errorMessage = if (showError && state.patient.age == 0) "This field is required" else null
                              )

            GenderSelectionCard(
                header = "Sex:",
                state = state,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.SEX, newValue)) },
                isError = showError && state.patient.sex.isBlank(),
                errorMessage = if (showError && state.patient.sex.isBlank()) "This field is required" else null
                               )

            NormalInputCard(
                header = "Occupation:",
                value = state.patient.occupation,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.OCCUPATION, newValue)) },
                isError = showError && state.patient.occupation.isBlank(),
                errorMessage = if (showError && state.patient.occupation.isBlank()) "This field is required" else null
                           )

            MaritalStatusSelectionCard(
                header = "Marital Status:",
                state = state,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.MARITAL_STATUS, newValue)) },
                isError = showError && state.patient.maritalStatus.isBlank(),
                errorMessage = if (showError && state.patient.maritalStatus.isBlank()) "This field is required" else null
                                      )

            NormalInputCard(
                header = "Address:",
                value = state.patient.address,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.ADDRESS, newValue)) },
                isError = showError && state.patient.address.isBlank(),
                errorMessage = if (showError && state.patient.address.isBlank()) "This field is required" else null
                           )

            NormalInputCard(
                header = "Tribe:",
                value = state.patient.tribe,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.TRIBE, newValue)) },
                isError = showError && state.patient.tribe.isBlank(),
                errorMessage = if (showError && state.patient.tribe.isBlank()) "This field is required" else null
                           )
        }

        if (isOn) {
            AlertDialog(
                onDismissRequest = { isOn = !isOn },
                onConfirmation = {
                    navController.navigate(Screens.HOME.name)
                    onEvent(FolderEvent.ResetAdd)
                    isOn = false
                },
                dismissText = "cancel",
                confirmText = "Yes",
                dialogTitle = "Info",
                dialogText = "Are you sure you want to go back"
                       )
        }

        BackHandler(
            enabled = true,
            onBack = { isOn = !isOn }
                   )
    }
}
