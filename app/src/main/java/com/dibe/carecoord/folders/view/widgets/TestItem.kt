package com.dibe.carecoord.folders.view.widgets

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

@Composable
fun TestItem(state : FolderState , index : Int, onEvent : (FolderEvent) -> Unit){
    ListItem(
        headlineContent = {
            (state.predictedTests+state.testSearchResult)[index].name.let { Text(text = it) }
                          } ,
        trailingContent = {
            (state.predictedTests+state.testSearchResult)[index].let { Text(text = "${it.currency}${it.price}") }
        },
        supportingContent = {
            Text(text = (state.predictedTests+state.testSearchResult)[index].category, fontSize = 10.sp, fontWeight = FontWeight.Light)
        },

        modifier = Modifier
            .background(color = Color.Blue)
            )
    HorizontalDivider(
        thickness = 2.dp
                     )
}
