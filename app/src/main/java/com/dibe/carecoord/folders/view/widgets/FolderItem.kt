package com.dibe.carecoord.old.Presentation.Screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderItem(state : FolderState , index : Int , onEvent : (FolderEvent) -> Unit , navController : NavController){
    ListItem(
        headlineContent = { state.folders[index].name.let { Text(text = it) } } ,
        trailingContent = {
            if (!state.onSelecting){
                IconButton(onClick = {
                    navController.navigate(Screens.EDIT.name)
                    onEvent(FolderEvent.StartEditing(state.folders[index]))
                }) {
                    Icon(Icons.AutoMirrored.Filled.NavigateNext , contentDescription ="next page")
                }
            }
            else {
                var onChecked by remember {
                    mutableStateOf(false)
                }

                Checkbox(
                    checked = onChecked ,
                    onCheckedChange = {
                        if (onChecked) {
                            onEvent(FolderEvent.OnUnselect(state.folders[index]))
                        }else {
                            onEvent(FolderEvent.OnSelect(state.folders[index]))
                        }
                        onChecked = !onChecked
                    },
                        )
            }
        } ,
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    navController.navigate(Screens.EDIT.name)
                    onEvent(FolderEvent.StartEditing(state.folders[index]))
                } ,
                onLongClick = {
                    onEvent(FolderEvent.StartSelecting)
                }
                              )
            .height(50.dp)
            .background(color = Color.Blue)
            )
    HorizontalDivider(
        thickness = 2.dp
                     )
}

