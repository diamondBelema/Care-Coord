package com.dibe.carecoord.chat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dibe.carecoord.auth.viewmodel.AuthEvent
import com.dibe.carecoord.auth.viewmodel.AuthState
import com.dibe.carecoord.chat.viewmodel.util.Chat
import com.dibe.carecoord.folders.viewmodel.FolderViewModel
import com.dibe.carecoord.folders.viewmodel.FolderViewModel.Companion
import com.dibe.carecoord.folders.viewmodel.util.Folder
import com.dibe.carecoord.utilities.AppwriteClient
import com.dibe.carecoord.utilities.SharedPreferencesHelper
import io.appwrite.Query
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(application: Application) : AndroidViewModel(application)  {

    private val _state = MutableStateFlow(ChatState())
    val state = _state.stateIn(viewModelScope , SharingStarted.WhileSubscribed(5000) , ChatState())

    private val account = Account(AppwriteClient.getClient())
    private val database = Databases(AppwriteClient.getClient())

    // Database and Collection IDs
    companion object {
        private const val DATABASE_ID = "671f7b31000a68d97864"
        private const val COLLECTION_ID = "6721f0f3001da4a2ebf6"
    }

    init {
        viewModelScope.launch {
            if (SharedPreferencesHelper.isLoggedIn() == true) {
                SharedPreferencesHelper.getHospitalID()?.let { startFetchingChats(it) }
            }
        }
    }

    fun onEvent(event: ChatEvent) {
        viewModelScope.launch {
            when (event) {
                is ChatEvent.SendMessage -> sendMessage(event.chat)
            }
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            _state.value.snackbarHostState.showSnackbar(message)
        }
    }

    // Function to fetch folders periodically
    private fun startFetchingChats(hospitalId: String) {
        viewModelScope.launch {
            while (true) {
                try {
                    val chats = withContext(Dispatchers.IO) {
                        database.listDocuments(
                            databaseId = DATABASE_ID ,
                            collectionId = COLLECTION_ID ,
                            queries = listOf(Query.equal("HospitalID" , listOf(hospitalId))) // Assumes 'hospitalId' is a filterable attribute
                                              )
                    }
                    val chatList = chats.documents.map { document -> Chat.fromDocument(document) } // Convert documents to Folder objects
                    _state.value = _state.value.copy(chats = chatList)
                } catch (e: Exception) {
                    // Handle error
                    e.printStackTrace()
                    showError("Error searching for folders: ${e.message}")
                }
                delay(30000) // Poll every 30 seconds
            }
        }
    }

    private suspend fun sendMessage(chat: Chat) {
        try {
            chat.hospitalId = SharedPreferencesHelper.getHospitalID().toString()
            val newChatDocument = database.createDocument(
                databaseId = DATABASE_ID ,
                collectionId = COLLECTION_ID ,
                documentId = "unique()" , // Use Appwrite's unique document ID
                data = chat.toMap() // Convert the Folder object to a Map<String, Any>
                                                           )
            val newChat = chat.copy(id = newChatDocument.id)
            _state.value = _state.value.copy(chats = _state.value.chats.plus(newChat))
        } catch (e: Exception) {
            // Handle error
            e.printStackTrace()
            showError("Error sending message")
        }
    }
}