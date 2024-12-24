package com.dibe.carecoord.chat.viewmodel.util

import io.appwrite.models.Document

data class Chat(
        var id : String = "" ,
        var senderId : String = "" ,
        var hospitalId : String = "" ,
        val senderName : String = "" ,
        val message : String = "" ,
        val timestamp : Long = 0
               ) {
    companion object {
        fun fromDocument(document: Document<Map<String , Any>>): Chat {
            return Chat(
                id = document.id ,
                senderId = document.data["SenderID"] as? String ?: "" ,
                hospitalId = document.data["HospitalID"] as? String ?: "" ,
                senderName = document.data["SenderName"] as? String ?: "" ,
                message = document.data["Message"] as? String ?: "" ,
                timestamp = document.data["Timestamp"] as? Long ?: 0 ,
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
