package com.dibe.carecoord.chat.viewmodel

import com.dibe.carecoord.chat.viewmodel.util.Chat

interface ChatEvent {
    data class SendMessage(val chat: Chat) : ChatEvent
}