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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import com.dibe.carecoord.folders.view.widgets.Header
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.folders.viewmodel.util.PatientField
import com.dibe.carecoord.old.Presentation.Screens.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisAndTreatmentScreen(
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
                title = { Text(text = stringResource(id = R.string.diagnosis_and_treatment_screen)) } ,
                navigationIcon = {
                    IconButton(onClick = {
                        isOn = !isOn
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack , contentDescription = "the back")
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
                        .padding(10.dp) ,
                    onClick = { navController.navigateUp() }
                              ) {
                    Text(text = "Back")
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp) ,
                    onClick = {
                        navController.navigate(Screens.LABORATORY_TESTS.name)
                        onEvent(FolderEvent.GetPossibleTests(state.patient))
//                        isSaveOn = !isSaveOn
                    }
                      ) {
                    Text(text = "Next")
                }
            }
        },
        snackbarHost = { SnackbarHost(state.snackbarHostState) }
            ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.Top ,
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.ime)
              ) {
            NormalInputCard(
                header = "Diagnosis:" ,
                value = state.patient.diagnosis ,
                onValueChange = { newValue ->
                    onEvent(
                        FolderEvent.ChangeValue(
                            PatientField.DIAGNOSIS ,
                            newValue
                                               )
                           )
                } ,
                maxLines = 10
                           )

            NormalInputCard(
                header = "Treatment:" ,
                value = state.patient.treatment ,
                onValueChange = { newValue ->
                    onEvent(
                        FolderEvent.ChangeValue(
                            PatientField.TREATMENT ,
                            newValue
                                               )
                           )
                } ,
                maxLines = 10
                           )

            NormalInputCard(
                header = "Case Summary:" ,
                value = state.patient.summary ,
                onValueChange = { newValue ->
                    onEvent(
                        FolderEvent.ChangeValue(
                            PatientField.SUMMARY ,
                            newValue
                                               )
                           )
                } ,
                maxLines = 10
                           )
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                      ) {
                    Header("Suggestions:")
                    Text(text = ": ${state.suggestions}" , modifier = Modifier.padding(10.dp))
                }
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
                    confirmText = "Yes" ,
                    dialogTitle = "Info" ,
                    dialogText = if (state.creatingTimeLine) "Are you sure you want to go back to edit" else "Are you sure you want to go back to home"
                           )
            }
        }
    }
}