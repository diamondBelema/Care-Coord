package com.dibe.carecoord.chat.viewmodel

import com.dibe.carecoord.chat.viewmodel.util.Chat

data class ChatState (
    var chats : List<Chat> = emptyList()
                     )