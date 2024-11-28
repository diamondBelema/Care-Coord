package com.dibe.carecoord.folders.view

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.folders.viewmodel.util.Document
import com.dibe.carecoord.old.Presentation.Screens.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class , ExperimentalPermissionsApi::class)
@Composable
fun DocumentUploadScreen(
        onEvent: (FolderEvent) -> Unit,
        state: FolderState,
        navController: NavController
                        ) {
    var isSaveOn by remember { mutableStateOf(false) }
    var isOn by remember { mutableStateOf(false) }

    // Permission state for external storage
    val permissionState1 = rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val permissionState2 = rememberPermissionState(permission = Manifest.permission.READ_MEDIA_IMAGES)

    // Request permission on launch
    SideEffect {
        permissionState1.launchPermissionRequest()
        permissionState2.launchPermissionRequest()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Upload Documents") },
                navigationIcon = {
                    IconButton(onClick = { isOn = true }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                        // Trigger the upload event
                        isSaveOn = !isSaveOn
                        onEvent(FolderEvent.UploadDocuments)
                    }
                      ) {
                    Text(text = "Upload")
                }
            }
        },
        snackbarHost = { SnackbarHost(state.snackbarHostState) }
            ) { paddingValues ->
        Column( Modifier.padding(paddingValues) ) {
            LazyColumn(contentPadding = PaddingValues()) {
                items(state.documentList.size) { index ->
                    // File picker launcher outside LazyColumn to avoid re-creating for each item
                    val filePickerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent(),
                        onResult = { uri: Uri? ->
                            uri?.let {
                                onEvent(FolderEvent.DocumentSelected(listOf(uri) , index))
                            }
                        }
                                                                              )

                    DocumentItem(
                        document = state.documentList[index],
                        onPickDocument = {
                            // Launch file picker to select a single document
                            filePickerLauncher.launch("application/*")
                                         },
                            onDelete = {
                                onEvent(FolderEvent.DeleteDocument(index))
                            }
                                    )
                    }
            }

            if (state.documentList.isNotEmpty()) {
                Text(
                    text = "Total Documents: ${state.documentList.size}" ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp) ,
                    fontSize = 20.sp ,
                    fontWeight = FontWeight.Medium
                    )
            }
        }
    }
    if (isSaveOn) {
        com.dibe.carecoord.folders.view.widgets.AlertDialog(
            onDismissRequest = { isSaveOn = ! isSaveOn } ,
            onConfirmation = {
                if (state.creatingTimeLine) {
                    navController.navigate(Screens.EDIT.name)
                } else{
                    navController.navigate(Screens.HOME.name)
                }
                onEvent(FolderEvent.Insert(state.patient))
                onEvent(FolderEvent.ResetAdd)
                isSaveOn = false
            } ,
            dismissText = "Cancel" ,
            confirmText = "Yes" ,
            dialogTitle = "Info" ,
            dialogText = "Are you sure you want to upload"
                                                           )
    }

    if (isOn) {
        com.dibe.carecoord.folders.view.widgets.AlertDialog(
            onDismissRequest = { isOn = ! isOn } ,
            onConfirmation = {
                if (state.creatingTimeLine) {
                    navController.navigate(Screens.EDIT.name)
                    onEvent(FolderEvent.ResetAdd)
                } else{
                    navController.navigate(Screens.HOME.name)
                }

                isOn = false
            } ,
            dismissText = "cancel" ,
            confirmText = "Yes" ,
            dialogTitle = "Info" ,
            dialogText = if (state.creatingTimeLine) "Are you sure you want to go back to edit" else "Are you sure you want to go back to home"
                                                           )
    }
}

@Composable
fun DocumentItem(
        document: Document,
        onPickDocument: () -> Unit,
        onDelete: () -> Unit
                ) {
    ListItem(
        headlineContent = {
            Text(
                text = document.name,
                fontWeight = FontWeight.Medium,
                )
        },
        trailingContent = {
            if (document.uri != null) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete document")
                }
            } else {
                IconButton(onClick = onPickDocument) {
                    Icon(Icons.Outlined.UploadFile, contentDescription = "Upload document")
                }
            }
        },

        supportingContent = {
            if (document.uri != null) {
                Text(
                    text = document.id,
                    fontWeight = FontWeight.Light,
                    fontSize = 15.sp
                    )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            )
    androidx.compose.material3.HorizontalDivider(
        thickness = 2.dp
                                                )
}
