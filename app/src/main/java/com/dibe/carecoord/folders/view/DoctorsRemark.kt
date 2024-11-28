package com.dibe.carecoord.folders.view

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dibe.carecoord.R
import com.dibe.carecoord.Screens.NormalInputCard
import com.dibe.carecoord.folders.view.widgets.AlertDialog
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.folders.viewmodel.util.PatientField
import com.dibe.carecoord.old.Presentation.Screens.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsRemark(
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
                title = { Text(text = stringResource(id = R.string.doctors_remark_name)) },
                navigationIcon = {
                    IconButton(onClick = {
                        isOn = !isOn
                    }) {
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
                    onClick = { navController.navigateUp() }
                              ) {
                    Text(text = "Back")
                }
                Button(
                    modifier = Modifier.weight(1f).padding(10.dp),
                    onClick = {
                        navController.navigate(Screens.DIAGNOSIS_AND_TREATMENT_SCREEN.name)
                        onEvent(FolderEvent.MakeDiagnosis(state.patient))
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
                header = "Complain:" ,
                value = state.patient.complain.toString() ,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.COMPLAIN , newValue)) } ,
                maxLines = 10
                           )

            NormalInputCard(
                header = "History Of Presenting Complain:" ,
                value = state.patient.hpc.toString() ,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.HPC, newValue)) } ,
                maxLines = 10
                           )

            NormalInputCard(
                header = "Past Medical / Surgical History:" ,
                value = state.patient.medicalHistory.toString() ,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.MEDICAL_HISTORY , newValue)) } ,
                maxLines = 10
                           )

            if (state.patient.sex == "Female"){
                NormalInputCard(
                    header = "Gynaecological History:" ,
                    value = state.patient.gynaeHistory.toString() ,
                    onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.GYNAE_HISTORY , newValue)) } ,
                    maxLines = 10
                               )
            }

            NormalInputCard(
                header = "Family / Social History:",
                value = state.patient.socialHistory.toString(),
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.SOCIAL_HISTORY, newValue)) },
                maxLines = 10
                           )

            NormalInputCard(
                header = "Drug History:",
                value = state.patient.drugHistory.toString(),
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.DRUG_HISTORY, newValue)) },
                maxLines = 10
                           )

            NormalInputCard(
                header = "Review Of System:",
                value = state.patient.ros.toString(),
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.ROS, newValue)) },
                maxLines = 10
                           )

            NormalInputCard(
                header = "Physical Examination:",
                value = state.patient.physicalExamination.toString(),
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.PHYSICAL_EXAMINATION, newValue)) },
                maxLines = 10
                           )

            NormalInputCard(
                header = "Investigation:",
                value = state.patient.investigation.toString(),
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.INVESTIGATION, newValue)) },
                maxLines = 10
                           )

            NormalInputCard(
                header = "Patient Update / Reviews:",
                value = state.patient.patientUpdate,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.UPDATE, newValue)) },
                maxLines = 10
                           )
        }
        if (isOn) {
            AlertDialog(
                onDismissRequest = { isOn = !isOn },
                onConfirmation = {
                    if (state.creatingTimeLine) {
                        navController.navigate(Screens.EDIT.name)
                    } else{
                        navController.navigate(Screens.HOME.name)
                    }
                    onEvent(FolderEvent.ResetAdd)
                    isOn = false
                },
                dismissText = "cancel",
                confirmText = "Yes",
                dialogTitle = "Info",
                dialogText = if (state.creatingTimeLine) "Are you sure you want to go back to edit" else "Are you sure you want to go back to home"
                       )
        }
    }
}
