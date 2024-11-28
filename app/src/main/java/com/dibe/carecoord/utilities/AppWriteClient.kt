package com.dibe.carecoord.utilities

import android.content.Context
import io.appwrite.Client

object AppwriteClient {

    // Lateinit property to hold the client instance
    private lateinit var client: Client

    // Initialize function for setting up the client instance
    fun initialize(context: Context) {
        client = Client(context).setProject("671be28f0000295bce99")
    }

    // Function to retrieve the client instance
    fun getClient(): Client {
        if (! AppwriteClient::client.isInitialized) {
            throw IllegalStateException("AppwriteClient is not initialized. Call initialize() first.")
        }
        return client
    }
}
