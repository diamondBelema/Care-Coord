package com.dibe.carecoord

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dibe.carecoord.auth.viewmodel.AuthViewModel
import com.dibe.carecoord.folders.viewmodel.FolderViewModel
import com.dibe.carecoord.utilities.AppwriteClient
import com.dibe.carecoord.utilities.SharedPreferencesHelper
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import io.appwrite.Client
import com.dibe.carecoord.ui.theme.CareCoordTheme
import com.dibe.carecoord.utilities.Nav
import io.appwrite.services.Account
import java.util.Collections


class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val folderViewModel: FolderViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        val configuration = RequestConfiguration.Builder()
            .setTestDeviceIds(Collections.singletonList("DEVICE ID"))
            .build()
        MobileAds.setRequestConfiguration(configuration)
        enableEdgeToEdge()

        setContent {
            AppwriteClient.initialize(this)
            SharedPreferencesHelper.initialize(this)

            CareCoordTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background
                       ) {
                    val authOnEvent = authViewModel::onEvent
                    val authState by authViewModel.state.collectAsState()
                    val folderOnEvent = folderViewModel::onEvent
                    val folderState by folderViewModel.state.collectAsState()

                    Nav(authOnEvent = authOnEvent, folderOnEvent = folderOnEvent, folderState = folderState, context = this)
                }
            }
        }
    }
}

