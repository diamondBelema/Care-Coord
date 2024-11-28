package com.dibe.carecoord.folders.viewmodel

import android.icu.text.CaseMap.Fold
import android.net.Uri
import com.dibe.carecoord.folders.viewmodel.util.Folder
import com.dibe.carecoord.folders.viewmodel.util.PatientField

interface FolderEvent {
    object ResetEdit : FolderEvent

    object ResetAdd : FolderEvent

    object StopCreatingTimeline : FolderEvent

    object UploadDocuments : FolderEvent

    object CreateTimeline : FolderEvent

    data class SelectTab(val index: Int): FolderEvent

    data class StartEditing(val folder : Folder) : FolderEvent

    data class ChangeSearch(val search: String): FolderEvent

    data class ChangeTestSearch(val search: String): FolderEvent

    data class Insert(val patient : Folder) : FolderEvent

    data class GetPossibleTests(val patient : Folder) : FolderEvent

    data class GetPossibleDocuments(val patient : Folder) : FolderEvent

    data class Delete(val patient : Folder) : FolderEvent

    data class Update(val patient : Folder , val editedPatient : Folder) : FolderEvent

    data class Search(val query: String) : FolderEvent

    data class SearchTest(val query: String) : FolderEvent

    data class MakeDiagnosis(val patient : Folder): FolderEvent

    data class ChangeValue(val field: PatientField , val value: Any) : FolderEvent

    data class ChangeEditValue(val field: PatientField , val value: Any, val index: Int) : FolderEvent

    data class OnSelect(val patient : Folder) : FolderEvent

    data class OnUnselect(val patient : Folder) : FolderEvent

    data class DocumentSelected(val uris: List<Uri> , val index: Int) : FolderEvent

    data class DeleteDocument(val index: Int) : FolderEvent

    data object StartSelecting : FolderEvent

    object DeleteSelectedFolder : FolderEvent
}