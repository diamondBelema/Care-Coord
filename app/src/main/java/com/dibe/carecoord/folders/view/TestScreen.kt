package com.dibe.carecoord.folders.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dibe.carecoord.R
import com.dibe.carecoord.folders.view.widgets.AlertDialog
import com.dibe.carecoord.folders.view.widgets.TestItem
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.old.Presentation.Screens.FolderItem
import com.dibe.carecoord.old.Presentation.Screens.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaboratoryTests(onEvent: (FolderEvent) -> Unit , state: FolderState , navController: NavController) {
    var isOn by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.laboratory_tests_name)) } ,
                navigationIcon = {
                    IconButton(onClick = {
                        isOn = true
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack , contentDescription = "the back")
                    }
                }
                     )
        } ,
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
                    onClick = {
                        navController.navigateUp()
                    }
                              ) {
                    Text(text = "Back")
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp) ,
                    onClick = {
                        navController.navigate(Screens.RESULTS_SCREEN.name)
                        onEvent(FolderEvent.GetPossibleDocuments(state.patient))
                    }
                      ) {
                    Text(text = "Next")
                }
            }
        } ,
        snackbarHost = { SnackbarHost(state.snackbarHostState) }
            ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Center
              ) {
            SearchTests(onEvent = onEvent , state = state)
            LazyColumn (
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier
                    .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.ime) ,
                       ){
                items((state.predictedTests+state.testSearchResult).size) {
                    TestItem(
                        state = state ,
                        index = it ,
                        onEvent = onEvent
                        
                              )
                }
            }

            if (state.predictedTests.isNotEmpty()) {
                Text(
                    text = "Total: ${
                        try {
                            state.predictedTests.first().currency
                        } catch (e : Exception) {
                            0
                        }
                    }${state.totalPrice}" ,
                    modifier = Modifier.fillMaxWidth().padding(10.dp) ,
                    fontSize = 30.sp ,
                    fontWeight = FontWeight.Medium
                    )
            }
        }
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

@Composable
fun SearchTests(onEvent: (FolderEvent) -> Unit , state: FolderState){
    OutlinedTextField(
        value = state.toSearchTest ,
        onValueChange = {
            onEvent(FolderEvent.ChangeTestSearch(it))
            onEvent(FolderEvent.SearchTest(state.toSearchTest))
        } ,
        placeholder = {
            Text(text = "Search tests")
        } ,
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = "Search Icon")
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.Search, contentDescription = "Search Icon")
            }
        } ,
        maxLines = 1 ,
        shape = RoundedCornerShape(16.dp) ,
        modifier = Modifier
            .height(75.dp)
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                     )
}
