package com.dibe.carecoord.auth.viewmodel

data class AuthState (
    val userEmailLogin: String = "",
    val userPasswordLogin: String = "",
    val hospitalPasswordLogin: String = "",

    val userNameSignUp: String = "",
    val userEmailSignUp: String = "",
    val userPasswordSignUp: String = "",
    val bio: String = "",
    val isEmailVerified: Boolean = false,
    val occupation: String = "",
    val hospitalPasswordSignUp : String = "",

    val hospitalName: String = "",
    val hospitalPassword: String = ""
                     )