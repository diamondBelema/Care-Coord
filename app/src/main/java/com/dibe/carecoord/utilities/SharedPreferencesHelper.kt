package com.dibe.carecoord.utilities

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("MyPreferences" , Context.MODE_PRIVATE)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString("username", username).apply()
    }

    fun saveBio(bio: String) {
        sharedPreferences.edit().putString("bio", bio).apply()
    }

    fun saveOccupation(occupation: String) {
        sharedPreferences.edit().putString("occupation", occupation).apply()
    }

    fun saveUserEmail(userEmail: String) {
        sharedPreferences.edit().putString("userEmail", userEmail).apply()
    }

    fun saveHospitalID(hospitalID: String) {
        sharedPreferences.edit().putString("hospitalID", hospitalID).apply()
    }

    fun setLoggedInStatus(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean? {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("username", "defaultUsername")
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("userEmail", "defaultUserEmail")
    }

    fun getBio(): String? {
        return sharedPreferences.getString("bio", "defaultBio")
    }

    fun getOccupation(): String? {
        return sharedPreferences.getString("occupation", "defaultOccupation")
    }

    fun getHospitalID(): String? {
        return sharedPreferences.getString("hospitalID", "defaultHospitalID")
    }

    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}
