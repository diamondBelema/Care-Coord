package com.dibe.carecoord.folders.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dibe.carecoord.R
import com.dibe.carecoord.folders.view.widgets.BloodPressureCard
import com.dibe.carecoord.folders.view.widgets.IntegerInputCard
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.folders.viewmodel.util.PatientField
import com.dibe.carecoord.folders.view.widgets.AlertDialog
import com.dibe.carecoord.folders.view.widgets.FloatInputCard
import com.dibe.carecoord.old.Presentation.Screens.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyParameters(
        onEvent: (FolderEvent) -> Unit ,
        state: FolderState ,
        navController: NavController
                  ) {
    var isOn by remember {
        mutableStateOf(false)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.body_parameters_name)) },
                navigationIcon = {
                    IconButton(onClick = { isOn = !isOn }) {
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    onClick = { navController.navigateUp() }
                              ) {
                    Text(text = "Back")
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    onClick = {
                        if ((state.patient.sex) != "Male") {
                            navController.navigate(Screens.FEMALE_DETAILS.name)
                        } else {
                            navController.navigate(Screens.DOCTORS_REMARK.name)
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
            FloatInputCard(
                header = "Height:",
                value = state.patient.height.toString(),
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.HEIGHT, newValue)) },
                trailingIcon = "M"
                            )
            FloatInputCard(
                header = "Weight:",
                value = state.patient.weight.toString(),
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.WEIGHT, newValue)) },
                trailingIcon = "KG"
                            )
            IntegerInputCard(
                header = "Pulse:",
                value = state.patient.pulse.toString(),
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.PULSE, newValue)) }
                            )
            FloatInputCard(
                header = "Oxygen Saturation (SpO2):",
                value = state.patient.oxygenSaturation.toString(),
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.OXYGEN_SATURATION, newValue)) }
                            )
            BloodPressureCard(
                sysValue = state.patient.sys.toString() ,
                onSysChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.SYS , newValue)) } ,
                dysValue = state.patient.dys.toString() ,
                onDysChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.DYS, newValue)) }
                             )
        }
        if (isOn) {
            AlertDialog(
                onDismissRequest = { isOn = ! isOn } ,
                onConfirmation = {
                    if (state.creatingTimeLine) {
                        navController.navigate(Screens.EDIT.name)
                    } else{
                        navController.navigate(Screens.HOME.name)
                    }
                    onEvent(FolderEvent.ResetAdd)
                    isOn = false
                } ,
                dismissText = "cancel" ,
                confirmText = "Go Back" ,
                dialogTitle = "Info" ,
                dialogText = if (state.creatingTimeLine) "Are you sure you want to go back to edit" else "Are you sure you want to go back to home"
                                                  )
        }
    }
}

