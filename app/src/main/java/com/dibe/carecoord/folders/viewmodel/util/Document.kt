package com.dibe.carecoord.folders.viewmodel.util
import android.net.Uri

data class Document(
        val id: String = "",         // Unique identifier for the document
        val name: String = "",       // Name of the document file
        val uri: Uri? = null,        // URI pointing to the document's location
        val size: Long = 0L,         // Size of the document in bytes
        val type: String = ""        // MIME type (e.g., "application/pdf")
                   )
