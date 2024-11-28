package com.dibe.carecoord.auth.view

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dibe.carecoord.auth.viewmodel.AuthEvent
import com.dibe.carecoord.folders.view.HomeScreen
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.utilities.SharedPreferencesHelper

@Composable
fun AuthOrMainScreen(
        navController: NavController ,
        authOnEvent: (AuthEvent) -> Unit ,
        folderOnEvent: (FolderEvent) -> Unit ,
        folderState: FolderState ,
        context: Context ,
                    ) {

    val shouldShowLoginScreen = SharedPreferencesHelper.isLoggedIn() != true

    if (shouldShowLoginScreen) {
        LoginScreen(onEvent = authOnEvent, context = context, navController = navController)
    } else {
        HomeScreen(onEvent = folderOnEvent , state = folderState , navController = navController , context = context)
    }
}
