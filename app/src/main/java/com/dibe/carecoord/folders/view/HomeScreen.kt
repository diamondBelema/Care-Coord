package com.dibe.carecoord.folders.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dibe.carecoord.R
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.old.Presentation.Screens.BottomNavigationBar
import com.dibe.carecoord.old.Presentation.Screens.FolderItem
import com.dibe.carecoord.old.Presentation.Screens.NoInternetConnectionScreen
import com.dibe.carecoord.old.Presentation.Screens.Screens
import com.dibe.carecoord.old.Presentation.Screens.isNetworkAvailable
import com.dibe.carecoord.old.Presentation.Screens.showInterstitial

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onEvent : (FolderEvent) -> Unit , state : FolderState , navController : NavController , context : Context) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        topBar = {
            if (state.onSelecting) {
                TopAppBar(
                    title = { Text(text = "${state.numberOfItemsSelected} items selected") },
                    navigationIcon = {
                        IconButton(onClick = { onEvent(FolderEvent.StartSelecting) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack , contentDescription = "stopping onSelected")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onEvent(FolderEvent.DeleteSelectedFolder) }) {
                            Icon(Icons.Default.Delete , contentDescription = "delete icon")
                        }
                    }
                         )
            } else {
                MediumTopAppBar(
                    title = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically ,
                            ){
                            Image(painter = painterResource(id = R.drawable.logo) , contentDescription = "Logo" , modifier = Modifier.size(25.dp))
                            Text(text = stringResource(id = R.string.app_name) , modifier = Modifier.absolutePadding(left = 10.dp))
                        }
                    },
                               )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screens.BASIC_DETAILS.name)
                showInterstitial(context){
                    navController.navigate(Screens.BASIC_DETAILS.name)
                }
            }) {
                Icon(Icons.Default.Add , contentDescription = "Add Icon")
            }
        },
        snackbarHost = { SnackbarHost(hostState = state.snackbarHostState) }
            ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
              ) {
            var text by remember { mutableStateOf("") }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(text = "Search notes") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search Icon") },
                enabled = false,
                readOnly = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(75.dp)
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { navController.navigate(Screens.SEARCH.name) }
                             )
            val isNetworkAvailable = remember { mutableStateOf(isNetworkAvailable(context)) }
            if (!isNetworkAvailable.value) {
                NoInternetConnectionScreen()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(4.dp) ,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.ime) ,
                          ) {
                    items(state.folders.size) { index ->
                        // Only display items where the name does NOT contain any digits
                        if (!state.folders[index].name.any { it.isDigit() }) {
                            FolderItem(
                                state = state,
                                index = index,
                                onEvent = onEvent,
                                navController = navController
                                      )
                        }
                    }
                }
            }
        }
    }
}

