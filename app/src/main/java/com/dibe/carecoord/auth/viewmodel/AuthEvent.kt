package com.dibe.carecoord.auth.viewmodel

interface AuthEvent {
    data class CreateUser(
            val name: String,
            val email: String,
            val password: String,
            val bio: String,
            val occupation: String,
            val hospitalID : String,
            val onSuccess: () -> Unit
                         ): AuthEvent

    data class LoginUser(
            val email: String,
            val password: String,
            val onSuccess: () -> Unit
                        ): AuthEvent
}