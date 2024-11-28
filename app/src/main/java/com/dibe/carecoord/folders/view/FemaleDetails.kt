package com.dibe.carecoord.folders.view

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
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
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.folders.viewmodel.util.PatientField
import com.dibe.carecoord.old.Presentation.Screens.Screens
import com.dibe.carecoord.folders.view.widgets.AlertDialog
import com.dibe.carecoord.folders.view.widgets.GravidityParityCard
import com.dibe.carecoord.folders.view.widgets.LmpAndEddCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FemaleDetails(onEvent: (FolderEvent) -> Unit, state: FolderState, navController: NavController, context: Context) {
    var isOn by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.female_details_name)) },
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
                    onClick = { navController.navigate(Screens.DOCTORS_REMARK.name) }
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
                .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.ime) ,
              ) {
            LmpAndEddCard(onEvent, state, context)

            GravidityParityCard(
                gravidity = state.patient.gravidity.toString(),
                parity = state.patient.parity.toString(),
                onValueChange = { gravidity, parity ->
                    onEvent(FolderEvent.ChangeValue(PatientField.GRAVIDITY, gravidity))
                    onEvent(FolderEvent.ChangeValue(PatientField.PARITY, parity))
                }
                               )

            NormalInputCard(
                header = "Katamenia:",
                value = state.patient.katamenia,
                onValueChange = { newValue -> onEvent(FolderEvent.ChangeValue(PatientField.KATAMENIA, newValue)) },
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
                dismissText = "Cancel",
                confirmText = "Yes",
                dialogTitle = "Info",
                dialogText = if (state.creatingTimeLine) "Are you sure you want to go back to edit" else "Are you sure you want to go back to home"
                       )
        }
    }
}

