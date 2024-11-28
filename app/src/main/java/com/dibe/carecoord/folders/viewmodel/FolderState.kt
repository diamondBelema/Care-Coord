package com.dibe.carecoord.folders.viewmodel

import androidx.compose.material3.SnackbarHostState
import com.dibe.carecoord.folders.viewmodel.util.Document
import com.dibe.carecoord.folders.viewmodel.util.Folder
import com.dibe.carecoord.folders.viewmodel.util.Test

data class FolderState(
        var patient: Folder = Folder() ,
        var editingPatient: List<Folder> = emptyList() ,
        var currentlyEditing: List<Folder> = emptyList() ,
        var onSelecting: Boolean = false ,
        var numberOfItemsSelected: Int = 0 ,
        var selectedItems: List<Folder> = emptyList() ,
        var toSearch: String = "" ,
        var toSearchTest: String = "" ,
        var isEditing: Boolean = false ,
        var suggestions: String = "",
        var documentList: List<Document> = emptyList(),
        var searchResult: List<Folder> = emptyList() ,
        var folders: List<Folder> = emptyList() ,
        var predictedTests: List<Test> = emptyList() ,
        var testSearchResult: List<Test> = emptyList() ,
        val totalPrice : String = "",
        var creatingTimeLine: Boolean = false,
        val snackbarHostState: SnackbarHostState = SnackbarHostState() ,
        var snackbarMessage: String = "" ,
        var selectedTabIndex: Int = 0
                      )
