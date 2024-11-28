package com.dibe.carecoord.folders.viewmodel

import android.net.Uri
import com.dibe.carecoord.folders.viewmodel.util.Document

object AvailableDocuments {

    val documents = listOf(
        Document(id = "doc1", name = "Ultrasound Scan", size = 15000, type = "application/pdf") ,
        Document(id = "doc2", name = "Pelvic Ultrasound", size = 4000, type = "application/pdf") ,
        Document(id = "doc3", name = "Abdominal Ultrasound", size = 4000, type = "application/pdf") ,
        Document(id = "doc4", name = "Prostate Ultrasound", size = 7000, type = "application/pdf") ,
        Document(id = "doc5", name = "Trans Vaginal Ultrasound Scan", size = 7000, type = "application/pdf") ,
        Document(id = "doc6", name = "Breast Scan", size = 10000, type = "application/pdf") ,
        Document(id = "doc7", name = "Ocular Scan", size = 10000, type = "application/pdf") ,
        Document(id = "doc8", name = "Soft Tissue Scan", size = 10000, type = "application/pdf") ,
        Document(id = "doc9", name = "Doppler Ultrasound", size = 20000, type = "application/pdf") ,
        Document(id = "doc10", name = "X-Ray", size = 10000, type = "application/pdf") ,
        Document(id = "doc11", name = "MRI", size = 70000, type = "application/pdf") ,
        Document(id = "doc12", name = "CT Scan", size = 60000, type = "application/pdf") ,
        Document(id = "doc13", name = "CT Angiography", size = 80000, type = "application/pdf") ,
        Document(id = "doc14", name = "Mammography", size = 25000, type = "application/pdf") ,
        Document(id = "doc15", name = "ECG", size = 10000, type = "application/pdf") ,
        Document(id = "doc16", name = "Echocardiogram", size = 30000, type = "application/pdf")
                          )

    // Function to retrieve the names of all documents
    fun getAllDocumentNames(): String {
        return documents.joinToString(separator = ", ") { it.name }
    }
}
