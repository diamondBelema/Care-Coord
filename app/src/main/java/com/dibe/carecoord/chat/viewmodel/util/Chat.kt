package com.dibe.carecoord.chat.viewmodel.util

import com.dibe.carecoord.folders.viewmodel.util.Folder
import io.appwrite.models.Document

data class Chat (
        val id: String = "",
        val senderId: Int = 0,
        val hospitalId: Int = 0,
        val senderName: String = "",
        val message: String = "",
        val timestamp: Int = 0
                ) {
    companion object {
        fun fromDocument(document: Document<Map<String , Any>>): Chat {
            return Chat(
                id = document.id,
                senderId = document.data["SenderID"] as? Int ?: 0,
                hospitalId = document.data["SenderID"] as? Int ?: 0,
                senderName = document.data["SenderID"] as? String ?: "",
                message = document.data["SenderID"] as? String ?: "",
                timestamp = document.data["SenderID"] as? Int ?: 0,
                       )
        }
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "SenderID" to senderId ,
            "HospitalID" to hospitalId ,
            "SenderName" to senderName ,
            "Message" to message ,
            "Timestamp" to timestamp
                    )
    }
}
