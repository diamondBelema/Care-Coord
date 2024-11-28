package com.dibe.carecoord.auth.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dibe.carecoord.utilities.AppwriteClient
import com.dibe.carecoord.utilities.SharedPreferencesHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.launch
import java.time.Instant
import android.util.Log

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AuthState())

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
                is AuthEvent.CreateUser -> createUser(event)
                is AuthEvent.LoginUser -> loginUser(event)
            }
        }
    }

    private suspend fun createUser(event: AuthEvent.CreateUser) {
        try {
            account.create("unique()", email = event.email, password = event.password, name = event.name)

            val sanitizedEmail = sanitizeEmail(event.email)

            database.createDocument(
                databaseId = DATABASE_ID,
                collectionId = COLLECTION_ID,
                documentId = sanitizedEmail,
                data = mapOf(
                    "HospitalID" to event.hospitalID,
                    "Email" to event.email,
                    "Name" to event.name,
                    "Bio" to event.bio,
                    "Occupation" to event.occupation,
                    "Timestamp" to Instant.now().toEpochMilli()
                            )
                                   )
            event.onSuccess()
        } catch (e: Exception) {
            logAndShowError("Error creating user", e)
        }
    }

    private suspend fun loginUser(event: AuthEvent.LoginUser) {
        try {
            account.createEmailPasswordSession(email = event.email, password = event.password)

            val sanitizedEmail = sanitizeEmail(event.email)

            val document = database.getDocument(
                databaseId = DATABASE_ID,
                collectionId = COLLECTION_ID,
                documentId = sanitizedEmail
                                               )

            SharedPreferencesHelper.setLoggedInStatus(true)
            SharedPreferencesHelper.saveUserEmail(event.email)
            SharedPreferencesHelper.saveHospitalID(document.data["HospitalID"] as String)
            SharedPreferencesHelper.saveBio(document.data["Bio"] as String)
            SharedPreferencesHelper.saveOccupation(document.data["Occupation"] as String)
            SharedPreferencesHelper.saveUsername(document.data["Name"] as String)

            event.onSuccess()
        } catch (e: Exception) {
            logAndShowError("Error logging in user", e)
        }
    }

    private suspend fun sendVerificationEmail() {
        try {
            account.createVerification(url = VERIFICATION_URL)
        } catch (e : Exception) {
            logAndShowError("Error sending verification email" , e)
        }
    }

    private suspend fun checkEmailVerificationStatus() {
        try {
            val user = account.get()
            _state.value = _state.value.copy(isEmailVerified = user.emailVerification)
        } catch (e: Exception) {
            logAndShowError("Error checking email verification status", e)
        }
    }

    // Helper function to sanitize email to use as a valid document ID
    private fun sanitizeEmail(email: String): String {
        return email.replace("@", "_at_").replace(".", "_dot_")
    }

    // Helper function to log the error and show a toast message
    private fun logAndShowError(message: String, exception: Exception) {
        Log.e("AuthViewModel", message, exception)
        Toast.makeText(getApplication(), exception.message ?: message, Toast.LENGTH_SHORT).show()
    }
}



