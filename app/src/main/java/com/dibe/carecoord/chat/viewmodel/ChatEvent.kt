package com.dibe.carecoord.chat.viewmodel

interface HDBEvent {
    data class SendMessage(
            val message: String,
            val onSuccess: () -> Unit,
            val onError: (Exception) -> Unit
                          ) : HDBEvent
}