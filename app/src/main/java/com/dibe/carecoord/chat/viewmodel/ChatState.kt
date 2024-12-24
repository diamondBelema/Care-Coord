package com.dibe.carecoord.chat.viewmodel

import androidx.compose.material3.SnackbarHostState
import com.dibe.carecoord.chat.viewmodel.util.Chat

data class ChatState (
        var chats : List<Chat> = emptyList() ,
        val snackbarHostState: SnackbarHostState = SnackbarHostState()
                     )