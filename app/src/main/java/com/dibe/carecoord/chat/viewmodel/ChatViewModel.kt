package com.dibe.carecoord.chat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dibe.carecoord.auth.viewmodel.AuthEvent
import com.dibe.carecoord.auth.viewmodel.AuthState
import com.dibe.carecoord.utilities.AppwriteClient
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application)  {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.stateIn(viewModelScope , SharingStarted.WhileSubscribed(5000) , AuthState())

    private val account = Account(AppwriteClient.getClient())
    private val database = Databases(AppwriteClient.getClient())

    // Database and Collection IDs
    companion object {
        private const val DATABASE_ID = "671f7b31000a68d97864"
        private const val COLLECTION_ID = "671f836900316fbbd4fb"
        private const val VERIFICATION_URL = "https://example.com"
    }

    fun onEvent(event: AuthEvent) {
        viewModelScope.launch {
            when (event) {

            }
        }
    }
}