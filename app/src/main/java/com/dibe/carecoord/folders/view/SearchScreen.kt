package com.dibe.carecoord.folders.view

import android.view.WindowInsets
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.old.Presentation.Screens.FolderItem

@Composable
fun SearchScreen(state: FolderState , onEvent: (FolderEvent) -> Unit , navController: NavController) {
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(150.dp)
               ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                }
                OutlinedTextField(
                    value = state.toSearch,
                    onValueChange = {
                        onEvent(FolderEvent.ChangeSearch(it))
                        onEvent(FolderEvent.Search(state.toSearch))
                    },
                    placeholder = {
                        Text(text = "Search notes")
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.Search, contentDescription = "Search Icon")
                        IconButton(onClick = { }) {
                            Icon(Icons.Outlined.Search, contentDescription = "Search Icon")
                        }
                    },
                    maxLines = 1,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(75.dp)
                        .padding(10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                                 )
            }
        }
            ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.ime) ,
            contentPadding = PaddingValues(4.dp)
                  ) {
            items(state.searchResult.size) {
                FolderItem(state = state, index = it, onEvent = onEvent, navController = navController)
            }
        }
    }
}
