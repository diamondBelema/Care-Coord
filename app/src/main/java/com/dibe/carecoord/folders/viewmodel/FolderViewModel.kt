package com.dibe.carecoord.folders.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.viewModelScope
import com.dibe.carecoord.folders.viewmodel.util.Document
import com.dibe.carecoord.folders.viewmodel.util.Folder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.exception.OpenAIHttpException
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.dibe.carecoord.folders.viewmodel.util.PatientField
import com.dibe.carecoord.folders.viewmodel.util.Test
import com.dibe.carecoord.utilities.AppwriteClient
import com.dibe.carecoord.utilities.SharedPreferencesHelper
import com.dibe.carecoord.utilities.UriPathFinder
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Databases
import io.appwrite.services.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FolderViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(FolderState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FolderState())

    private lateinit var documentPickerLauncher: ActivityResultLauncher<String>

    private val database = Databases(AppwriteClient.getClient())
    private val storage = Storage(AppwriteClient.getClient())
    private val openAI = OpenAI("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")
    private val uriPathFinder = UriPathFinder()

    // Database and Collection IDs
    companion object {
        private const val DATABASE_ID = "671f7b31000a68d97864"
        private const val COLLECTION_ID = "671f8b7e0009e3a582c3"
        private const val BUCKET_ID = "672a1d16001325f0bfe3"
    }

    init {
        viewModelScope.launch {
            if (SharedPreferencesHelper.isLoggedIn() == true) {
                SharedPreferencesHelper.getHospitalID()?.let { startFetchingFolders(it) }
            }
        }
    }

    fun onEvent(event: FolderEvent) {
        viewModelScope.launch {
            when (event) {
                is FolderEvent.Insert -> insertFolder(event.patient)
                is FolderEvent.Update -> updateFolder(event.patient, event.editedPatient)
                is FolderEvent.Delete -> deleteFolder(event.patient)
                is FolderEvent.Search -> searchFolders(event.query)
                is FolderEvent.OnSelect -> onSelect(event.patient)
                is FolderEvent.OnUnselect -> onUnselect(event.patient)
                is FolderEvent.GetPossibleTests -> getPossibleTests(event.patient.toMap(), AvailableTests.getAllTestNames())
                is FolderEvent.GetPossibleDocuments -> getPossibleDocuments(event.patient.toMap(), AvailableDocuments.getAllDocumentNames())
                is FolderEvent.ResetEdit -> resetEdit()
                is FolderEvent.ResetAdd -> resetAdd()
                is FolderEvent.MakeDiagnosis -> getDiagnosis(event.patient.toMap())
                is FolderEvent.ChangeSearch -> changeSearch(event)
                is FolderEvent.SearchTest -> searchTest(event)
                is FolderEvent.ChangeTestSearch -> changeTestSearch(event)
                is FolderEvent.DeleteDocument -> deleteDocument(event.index)
                is FolderEvent.ChangeValue -> handleChangeValue(event.field, event.value)
                is FolderEvent.UploadDocuments -> uploadDocuments()
                is FolderEvent.ChangeEditValue -> handleChangeEditValue(event.field, event.value, event.index)
                is FolderEvent.DocumentSelected -> onFilePathsListChange(event.uris, event.index) // Process selected document
                is FolderEvent.CreateTimeline -> createTimeline()
                is FolderEvent.StopCreatingTimeline -> stopCreatingTimeline()
                is FolderEvent.StartEditing -> startEditing(event.folder)
                is FolderEvent.SelectTab -> selectTab(event.index)
            }
        }
    }

    private fun selectTab(index: Int){
        _state.value = _state.value.copy(selectedTabIndex = index)
    }

    private fun createTimeline() {
        onEvent(FolderEvent.ChangeValue(PatientField.NAME, "${_state.value.editingPatient[0].name}${_state.value.editingPatient.size}"))
        onEvent(FolderEvent.ChangeValue(PatientField.AGE, state.value.editingPatient[0].age))
        onEvent(FolderEvent.ChangeValue(PatientField.SEX, state.value.editingPatient[0].sex))
        onEvent(FolderEvent.ChangeValue(PatientField.OCCUPATION, state.value.editingPatient[0].occupation))
        onEvent(FolderEvent.ChangeValue(PatientField.MARITAL_STATUS, state.value.editingPatient[0].maritalStatus))
        onEvent(FolderEvent.ChangeValue(PatientField.ADDRESS, state.value.editingPatient[0].address))
        onEvent(FolderEvent.ChangeValue(PatientField.TRIBE, state.value.editingPatient[0].tribe))

        _state.value = _state.value.copy(creatingTimeLine = true)
    }

    private fun startEditing(folder: Folder) {
        val folderTimelines = _state.value.folders.filter {
            it.name.contains(folder.name)
        }

        folderTimelines.forEach {
            _state.value = _state.value.copy(
                currentlyEditing = _state.value.currentlyEditing.plus(it),
                editingPatient = _state.value.editingPatient.plus(it),
                                            )
        }
    }

    private fun stopCreatingTimeline() {
        _state.value = _state.value.copy(
            creatingTimeLine = false,

                                        )
        resetAdd()
    }

    private fun onFilePathsListChange(list: List<Uri>, index: Int) {
        // Check if the list of URIs is empty before processing
        if (list.isEmpty()) return

        val updatedList = state.value.documentList.toMutableList()

        // Change URIs to Document list
        val documents = changeUriToDocuments(list, getApplication(), _state.value.documentList[index].name)

        // Ensure that index is within bounds before updating the document
        if (index < updatedList.size) {
            viewModelScope.launch {
                updatedList[index] = documents.firstOrNull() ?: return@launch
                _state.value = _state.value.copy(
                    documentList = updatedList
                                                )
            }
        }
    }


    private fun changeUriToDocuments(uris: List<Uri>, context: Context,  name: String): List<Document> {
        val documentsList = mutableListOf<Document>()

        uris.forEach { uri ->
            try {
                // Retrieve additional file details if available
                val document = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                    if (cursor.moveToFirst()) {
                        val fileName = if (nameIndex != -1) cursor.getString(nameIndex) ?: "Unknown" else "Unknown"
                        val size = if (sizeIndex != -1) cursor.getLong(sizeIndex) else 0L
                        val type = context.contentResolver.getType(uri) ?: "Unknown"

                        Document(
                            id = fileName,
                            name = name,
                            uri = uri,
                            size = size,
                            type = type
                                )
                    } else {
                        // Log or handle the case where the cursor has no data
                        showError("No data found for URI: $uri")
                        null
                    }
                }

                document?.let { documentsList.add(it) }
            } catch (e: Exception) {
                showError("Failed to process document for URI: $uri\nError: ${e.message}")
                Log.e("MyTag", "Failed to process document for URI: $uri\nError: ${e.message}")
                e.printStackTrace()
            }
        }

        return documentsList
    }



    private fun deleteDocument(index: Int) {
        if (index in _state.value.documentList.indices) {
            // Remove the document from the list
            val updatedDocuments = _state.value.documentList.toMutableList()
            updatedDocuments[index] = Document(
                id = "",
                name = _state.value.documentList[index].name,
                uri = null,
                size = 0L,
                type = ""
                                              )

            _state.value = _state.value.copy(
                documentList = updatedDocuments
                                            )

            // Update the state with the new list of documents
            _state.value = _state.value.copy(documentList = updatedDocuments)
        } else {
            Log.e("FolderViewModel", "Index out of bounds: $index")
        }
    }

    private fun changeTestSearch(event: FolderEvent.ChangeTestSearch) {
        _state.value = _state.value.copy(toSearchTest = event.search)
    }

    private fun searchTest(event: FolderEvent.SearchTest) {
        // Assuming AvailableTests.tests is a List<AvailableTest> (or similar) that contains the test data
        val searchedTests = AvailableTests.tests.filter { test ->
            test.name.contains(event.query, ignoreCase = true) // Check if test name contains the query
        }

        // Update the state with the filtered results
        _state.value = _state.value.copy(testSearchResult = searchedTests)
    }

    @OptIn(BetaOpenAI::class)
    suspend fun getPossibleDocuments(folderMap: Map<String, Any>, documentNames: String) {
        val prompt = buildString {
            append("Based on the following patient details: ")
            append(folderMap)
            append(" and the list of available documents: ")
            append(documentNames)
            append(". Return only the recommended documents as a comma-separated list while keeping each document name exactly as provided (e.g., Lab Report, Prescription, Medical History), or an empty string if no recommendation is necessary.")
        }

        try {
            val request = ChatCompletionRequest(
                model = ModelId("gpt-4o"),
                n = 1,
                messages = listOf(ChatMessage(role = ChatRole.User, content = prompt))
                                               )

            val response: Flow<ChatCompletionChunk> = openAI.chatCompletions(request)
            val messageBuilder = StringBuilder()

            response.collect { chunk ->
                Log.i("MyTag", chunk.choices.firstOrNull().toString())
                messageBuilder.append(chunk.choices.firstOrNull()?.delta?.content ?: "")

                val responseContent = messageBuilder.toString().trim()
                val possibleDocuments = responseContent.split(",").mapNotNull { docName ->
                    AvailableDocuments.documents.find { it.name.equals(docName.trim(), ignoreCase = true) }
                }

                // Update the document list in the state
                _state.value = _state.value.copy(documentList = possibleDocuments)
            }
        } catch (e: Exception) {
            showError("Error fetching data: ${e.message}")
            e.printStackTrace()
        } catch (e: OpenAIHttpException) {
            showError("Open AI Error: ${e.message}")
            e.printStackTrace()
        }
    }

    @OptIn(BetaOpenAI::class)
    suspend fun getPossibleTests(folderMap: Map<String, Any>, testNames: String) {
        val prompt = buildString {
            append("Based on the following patient details: ")
            append(folderMap)
            append(" and the list of laboratory tests: ")
            append(testNames)
            append(". Return only the recommended tests as a comma-separated list while keeping each test name exactly as provided (e.g., PCV, Blood Film for Malaria Parasite, WBC), or an empty string if no recommendation is necessary and the character limit should be 100.")
        }

        try {
            val request = ChatCompletionRequest(
                model = ModelId("gpt-4o"),
                n = 1,
                messages = listOf(ChatMessage(role = ChatRole.User, content = prompt))
                                               )

            val response: Flow<ChatCompletionChunk> = openAI.chatCompletions(request)
            val messageBuilder = StringBuilder()

            response.collect { chunk ->
                Log.i("MyTag" , chunk.choices.firstOrNull().toString())
                messageBuilder.append(chunk.choices.firstOrNull()?.delta?.content ?: "")

                val responseContent = messageBuilder.toString().trim()
                _state.value.patient = _state.value.patient.copy(tests = responseContent)
                val possibleTests = responseContent.split(",").mapNotNull { testName ->
                    AvailableTests.tests.find { it.name.equals(testName.trim(), ignoreCase = true) }
                }
                var totalPrice = 0
                possibleTests.forEach{
                    totalPrice = totalPrice.plus(it.price.toInt())
                }

                _state.value = _state.value.copy(predictedTests = possibleTests, totalPrice = totalPrice.toString())
            }
        } catch (e: Exception) {
            showError("Error fetching data: ${e.message}")
            e.printStackTrace()
        }
        catch (e: OpenAIHttpException) {
            showError("Open AI Error: ${e.message}")
            e.printStackTrace()
        }
    }

    @OptIn(BetaOpenAI::class)
    suspend fun getDiagnosis(folderMap: Map<String, Any>) {
        val diagnosisPrompt = buildString {
            append("Based on the following patient details: ")
            append(folderMap)
            append(". List 3 possible diagnoses and 3 differential diagnosis, ranked by likelihood or how common each one is given the symptoms, medical history, and examination findings. For each diagnosis, include a brief indication of its likelihood (e.g., 'common,' 'less common,' 'rare'). Only output the diagnosis list with likelihood indicators, without additional commentary and the character limit should be 400.")
        }

        val treatmentPrompt = buildString {
            append("Given the following patient details and the possible diagnosis: ")
            append(folderMap)
            append(". Suggest an appropriate treatment plan including medications, lifestyle recommendations, and follow-up if necessary also include drugs and its dosages based on the weight and age in the folder. Only output the treatment plan with no further explanation and the character limit should be 300.")
        }

        val summaryPrompt = buildString {
            append("Using the following patient details: ")
            append(folderMap)
            append(". Create a brief case summary focusing on the main complaint, medical background, examination findings, and diagnosis. Keep it concise and focused, with no extra details beyond the summary and the character limit should be 300.")
        }

        val suggestionsPrompt = buildString {
            append("Based on the following patient details: ")
            append(folderMap)
            append(". Using these details, provide a list of recommended suggestions, such as potential tests, lifestyle changes, monitoring needs, or any relevant actions that could benefit the patient. Focus on suggesting actions that could improve the patient's overall health or address specific issues noted in the data. Format each suggestion concisely in a list without additional commentary and the character limit should be 100.")
        }


        val diagnosisRequest = ChatCompletionRequest(
            model = ModelId("gpt-4o"),
            n = 1,
            messages = listOf(ChatMessage(role = ChatRole.User, content = diagnosisPrompt))
                                                    )

        val diagnosisResponse: Flow<ChatCompletionChunk> = openAI.chatCompletions(diagnosisRequest)
        val diagnosisBuilder = StringBuilder()

        diagnosisResponse.collect { chunk ->
            Log.i("MyTag", chunk.choices.firstOrNull().toString())
            diagnosisBuilder.append(chunk.choices.firstOrNull()?.delta?.content ?: "")

            val diagnosisContent = diagnosisBuilder.toString().trim()
            // Store or process diagnosis content as needed
            _state.value.patient = _state.value.patient.copy(diagnosis = diagnosisContent)
        }

        val treatmentRequest = ChatCompletionRequest(
            model = ModelId("gpt-4o"),
            n = 1,
            messages = listOf(ChatMessage(role = ChatRole.User, content = treatmentPrompt))
                                                    )

        val treatmentResponse: Flow<ChatCompletionChunk> = openAI.chatCompletions(treatmentRequest)
        val treatmentBuilder = StringBuilder()

        treatmentResponse.collect { chunk ->
            Log.i("MyTag", chunk.choices.firstOrNull().toString())
            treatmentBuilder.append(chunk.choices.firstOrNull()?.delta?.content ?: "")

            val treatmentContent = treatmentBuilder.toString().trim()
            // Store or process treatment content as needed
            _state.value.patient = _state.value.patient.copy(treatment = treatmentContent)
        }

        val summaryRequest = ChatCompletionRequest(
            model = ModelId("gpt-4o"),
            n = 1,
            messages = listOf(ChatMessage(role = ChatRole.User, content = summaryPrompt))
                                                  )

        val summaryResponse: Flow<ChatCompletionChunk> = openAI.chatCompletions(summaryRequest)
        val summaryBuilder = StringBuilder()

        summaryResponse.collect { chunk ->
            Log.i("MyTag", chunk.choices.firstOrNull().toString())
            summaryBuilder.append(chunk.choices.firstOrNull()?.delta?.content ?: "")

            val summaryContent = summaryBuilder.toString().trim()
            // Store or process summary content as needed
            _state.value.patient = _state.value.patient.copy(summary = summaryContent)
        }

        val suggestionsRequest = ChatCompletionRequest(
            model = ModelId("gpt-4o"),
            n = 1,
            messages = listOf(ChatMessage(role = ChatRole.User, content = suggestionsPrompt))
                                                      )

        val suggestionsResponse: Flow<ChatCompletionChunk> = openAI.chatCompletions(suggestionsRequest)
        val suggestionsBuilder = StringBuilder()

        suggestionsResponse.collect { chunk ->
            Log.i("MyTag", chunk.choices.firstOrNull().toString())
            suggestionsBuilder.append(chunk.choices.firstOrNull()?.delta?.content ?: "")

            val suggestionsContent = suggestionsBuilder.toString().trim()
            // Store or process suggestions content as needed
            _state.value = _state.value.copy(suggestions = suggestionsContent)
        }


    }

    private fun changeSearch(event: FolderEvent.ChangeSearch) {
        _state.value = _state.value.copy(toSearch = event.search)
    }

    private fun uriToFile(context: Context , uri: Uri): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "temp_file_${System.currentTimeMillis()}")
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        return if (tempFile.exists()) tempFile else null
    }

    private fun sanitizeFileName(fileName: String): String {
        // Replace any non-alphanumeric character with an underscore
        return fileName.replace("[^A-Za-z0-9]".toRegex(), "_")
            .take(50) // Limit the length to 50 characters if necessary
    }

    private fun uploadDocuments() {
        _state.value.documentList.forEach { document ->
            val documentFile = document.uri?.let { uriToFile(getApplication(), it) }
            viewModelScope.launch {
                try {
                    // Sanitize the document name
                    val sanitizedFileName = sanitizeFileName(document.name)

                    documentFile?.let { io.appwrite.models.InputFile.fromFile(it) }?.let {
                        storage.createFile(
                            bucketId = BUCKET_ID,
                            fileId = "${sanitizedFileName}xxx${sanitizeFileName(SharedPreferencesHelper.getHospitalID() ?: "")}xxx${sanitizeFileName(_state.value.patient.name)}",
                            file = it
                                          )
                    }

                } catch (e: AppwriteException) {
                    e.printStackTrace()
                    showError("Error inserting documents: ${e.message}")
                }
            }
        }
    }


    // Function to insert a new folder
    private suspend fun insertFolder(folder: Folder) {
        try {
            folder.hospitalId = SharedPreferencesHelper.getHospitalID().toString()
            val newFolderDocument = database.createDocument(
                databaseId = DATABASE_ID,
                collectionId = COLLECTION_ID,
                documentId = "unique()", // Use Appwrite's unique document ID
                data = folder.toMap() // Convert the Folder object to a Map<String, Any>
                                                           )
            val newFolder = folder.copy(id = newFolderDocument.id)
            _state.value = _state.value.copy(folders = _state.value.folders.plus(newFolder))
            showError("${newFolder.name} folder Successfully added")
        } catch (e: Exception) {
            // Handle error
            e.printStackTrace()
            showError("Error inserting ${folder.name} folder: ${e.message}")
        }
    }

    // Function to update an existing folder
    private suspend fun updateFolder(folder: Folder, editedFolder: Folder) {
        try {
            val updatedFolderDocument = withContext(Dispatchers.IO) {
                editedFolder.hospitalId = SharedPreferencesHelper.getHospitalID().toString()
                database.updateDocument(
                    databaseId = DATABASE_ID,
                    collectionId = COLLECTION_ID,
                    documentId = folder.id, // Use the existing folder ID to update
                    data = editedFolder.toMap() // Convert the updated folder data to a Map
                                       )
            }
            val updatedFolder = editedFolder.copy(id = updatedFolderDocument.id)
            val newFolder = _state.value.folders.toMutableList()
            newFolder[_state.value.folders.indexOf(folder)] = updatedFolder


            _state.value = _state.value.copy(
                folders =  newFolder,
                isEditing = false
                                            )
            showError("${updatedFolder.name} folder Successfully updated")
        } catch (e: AppwriteException) {
            // Handle error
            e.printStackTrace()
            showError("Error updating ${folder.name} folder: ${e.message}")
        }
    }

    private suspend fun deleteFolder(patient: Folder) {
        try {
            // Use Appwrite's deleteDocument method to remove the folder
            database.deleteDocument(
                databaseId = DATABASE_ID,
                collectionId = COLLECTION_ID,
                documentId = patient.id // patient.id is the unique document ID
                                   )

            // Update the state by filtering out the deleted folder
            _state.value = _state.value.copy(
                folders = _state.value.folders.filter { it.id != patient.id }
                                            )
            showError("${patient.name} folder Successfully deleted")
        } catch (e: Exception) {
            // Handle error and show a message if needed
            e.printStackTrace()
            showError("Error deleting ${patient.name} folder: ${e.message}")
        }
    }


    // Function to search folders
    private suspend fun searchFolders(query: String) {
        try {
            val results = withContext(Dispatchers.IO) {
                database.listDocuments(
                    databaseId = DATABASE_ID,
                    collectionId = COLLECTION_ID,
                    queries = listOf(
                        Query.contains("Name", listOf(query)),
                                    ) // Assumes 'name' is a searchable attribute
                                      )
            }
            val folders = results.documents.map { document -> Folder.fromDocument(document) } // Convert documents to Folder objects
            _state.value = _state.value.copy(searchResult = folders)
        } catch (e: AppwriteException) {
            // Handle error
            e.printStackTrace()
            showError("Error: ${e.message}")
        }
    }

    // Function to fetch folders periodically
    private fun startFetchingFolders(hospitalId: String) {
        viewModelScope.launch {
            while (true) {
                try {
                    val folders = withContext(Dispatchers.IO) {
                        database.listDocuments(
                            databaseId = DATABASE_ID,
                            collectionId = COLLECTION_ID,
                            queries = listOf(Query.equal("HospitalID", listOf(hospitalId))) // Assumes 'hospitalId' is a filterable attribute
                                              )
                    }
                    val folderList = folders.documents.map { document -> Folder.fromDocument(document) } // Convert documents to Folder objects
                    _state.value = _state.value.copy(folders = folderList)
                } catch (e: Exception) {
                    // Handle error
                    e.printStackTrace()
                    showError("Error searching for folders: ${e.message}")
                }
                delay(30000) // Poll every 30 seconds
            }
        }
    }



    private fun resetEdit() {
        _state.value = _state.value.copy(
            isEditing = false,
            editingPatient = emptyList()
                                        )
    }

    private fun resetAdd() {
        _state.value = _state.value.copy(patient = Folder(), creatingTimeLine = false)
    }

    private fun onSelect(patient: Folder) {
        _state.value = _state.value.copy(
            selectedItems = _state.value.selectedItems.plus(patient),
            numberOfItemsSelected = (_state.value.numberOfItemsSelected) + 1
                                        )
    }

    private fun onUnselect(patient: Folder) {
        _state.value = _state.value.copy(
            selectedItems = _state.value.selectedItems.filter { it != patient },
            numberOfItemsSelected = (_state.value.numberOfItemsSelected) - 1
                                        )
    }

    private fun handleChangeValue(field: PatientField , newValue: Any){
        val currentState = _state.value
        val updatedPatient = when (field) {
            PatientField.NAME -> (newValue as String?)?.let { currentState.patient.copy(name = it) }
            PatientField.AGE -> ("$newValue".toInt() as Int?)?.let {
                currentState.patient.copy(
                    age = it
                                         )
            }

            PatientField.SEX -> (newValue as String?)?.let { currentState.patient.copy(sex = it) }
            PatientField.OCCUPATION -> (newValue as String?)?.let {
                currentState.patient.copy(
                    occupation = it
                                         )
            }

            PatientField.MARITAL_STATUS -> (newValue as String?)?.let {
                currentState.patient.copy(
                    maritalStatus = it
                                         )
            }

            PatientField.ADDRESS -> (newValue as String?)?.let {
                currentState.patient.copy(
                    address = it
                                         )
            }

            PatientField.TRIBE -> (newValue as String?)?.let { currentState.patient.copy(tribe = it) }
            PatientField.HEIGHT -> {
                val height = "$newValue".toFloat() as Float?
                val weight = _state.value.patient.weight
                currentState.patient.copy(
                    height = height ?: 0.0f , bmi = weight.div((height?.times(height) !!))
                                         )
            }

            PatientField.WEIGHT -> currentState.patient.copy(weight = "$newValue".toFloat())
            PatientField.BMI -> {
                val weight = "$newValue".toFloat()
                val height = _state.value.patient.height
                currentState.patient.copy(
                    height = height , bmi = weight.div((height.times(height)))
                                         )
            }

            PatientField.PULSE -> currentState.patient.copy(pulse = "$newValue".toInt())
            PatientField.OXYGEN_SATURATION -> currentState.patient.copy(oxygenSaturation = "$newValue".toFloat())
            PatientField.SYS -> currentState.patient.copy(sys = "$newValue".toInt())
            PatientField.DYS -> currentState.patient.copy(dys = "$newValue".toInt())
            PatientField.LMP -> (newValue as String?)?.let { currentState.patient.copy(lmp = it) }
            PatientField.EDD -> (newValue as String?)?.let { currentState.patient.copy(edd = it) }
            PatientField.GRAVIDITY -> currentState.patient.copy(gravidity = newValue as String)
            PatientField.PARITY -> currentState.patient.copy(parity = newValue as String)
            PatientField.KATAMENIA -> (newValue as String?)?.let { currentState.patient.copy(katamenia = it) }
            PatientField.COMPLAIN -> currentState.patient.copy(complain = newValue as String)
            PatientField.HPC -> currentState.patient.copy(hpc = newValue as String)
            PatientField.GYNAE_HISTORY -> currentState.patient.copy(gynaeHistory = newValue as String)
            PatientField.MEDICAL_HISTORY -> currentState.patient.copy(medicalHistory = newValue as String)
            PatientField.SOCIAL_HISTORY -> currentState.patient.copy(socialHistory = newValue as String)
            PatientField.DRUG_HISTORY -> currentState.patient.copy(drugHistory = newValue as String)
            PatientField.ROS -> currentState.patient.copy(ros = newValue as String)
            PatientField.PHYSICAL_EXAMINATION -> currentState.patient.copy(physicalExamination = newValue as String)
            PatientField.DIAGNOSIS -> currentState.patient.copy(diagnosis = newValue as String)
            PatientField.INVESTIGATION -> currentState.patient.copy(investigation = newValue as String)
            PatientField.TREATMENT -> currentState.patient.copy(treatment = newValue as String)
            PatientField.SUMMARY -> currentState.patient.copy(summary = newValue as String)
            PatientField.UPDATE -> currentState.patient.copy(patientUpdate = newValue as String)
        }
        _state.value = updatedPatient.let { currentState.copy(patient = it ?: _state.value.patient) }
    }

    private fun handleChangeEditValue(field: PatientField, newValue: Any, index: Int) {
        val currentState = _state.value
        val updatedPatient = when (field) {
            PatientField.NAME -> (newValue as String).let { currentState.editingPatient[index].copy(name = it) }

            PatientField.AGE -> ("$newValue".toInt()).let {
                currentState.editingPatient[index].copy(age = it)
            }

            PatientField.SEX -> (newValue as String).let { currentState.editingPatient[index].copy(sex = it) }

            PatientField.OCCUPATION -> (newValue as String).let {
                currentState.editingPatient[index].copy(occupation = it)
            }

            PatientField.MARITAL_STATUS -> (newValue as String).let {
                currentState.editingPatient[index].copy(maritalStatus = it)
            }

            PatientField.ADDRESS -> (newValue as String).let {
                currentState.editingPatient[index].copy(address = it)
            }

            PatientField.TRIBE -> (newValue as String).let { currentState.editingPatient[index].copy(tribe = it) }

            PatientField.HEIGHT -> {
                val height = "$newValue".toFloat()
                val weight = _state.value.editingPatient[index].weight
                currentState.editingPatient[index].copy(
                    height = height, bmi = weight.div((height.times(height)))
                                                       )
            }

            PatientField.WEIGHT -> currentState.editingPatient[index].copy(weight = "$newValue".toFloat())

            PatientField.BMI -> {
                val weight = "$newValue".toFloat()
                val height = _state.value.editingPatient[index].height
                currentState.editingPatient[index].copy(
                    height = height, bmi = weight.div((height.times(height)))
                                                       )
            }

            PatientField.PULSE -> currentState.editingPatient[index].copy(pulse = "$newValue".toInt())
            PatientField.OXYGEN_SATURATION -> currentState.editingPatient[index].copy(oxygenSaturation = "$newValue".toFloat())
            PatientField.SYS -> currentState.editingPatient[index].copy(sys = "$newValue".toInt())
            PatientField.DYS -> currentState.editingPatient[index].copy(dys = "$newValue".toInt())

            PatientField.LMP -> (newValue as String).let { currentState.editingPatient[index].copy(lmp = it) }
            PatientField.EDD -> (newValue as String).let { currentState.editingPatient[index].copy(edd = it) }

            PatientField.GRAVIDITY -> currentState.editingPatient[index].copy(gravidity = newValue as String)
            PatientField.PARITY -> currentState.editingPatient[index].copy(parity = newValue as String)

            PatientField.KATAMENIA -> (newValue as String).let { currentState.editingPatient[index].copy(katamenia = it) }

            PatientField.COMPLAIN -> currentState.editingPatient[index].copy(complain = newValue as String)
            PatientField.HPC -> currentState.editingPatient[index].copy(hpc = newValue as String)

            PatientField.GYNAE_HISTORY -> currentState.editingPatient[index].copy(gynaeHistory = newValue as String)
            PatientField.MEDICAL_HISTORY -> currentState.editingPatient[index].copy(medicalHistory = newValue as String)

            PatientField.SOCIAL_HISTORY -> currentState.editingPatient[index].copy(socialHistory = newValue as String)
            PatientField.DRUG_HISTORY -> currentState.editingPatient[index].copy(drugHistory = newValue as String)

            PatientField.ROS -> currentState.editingPatient[index].copy(ros = newValue as String)
            PatientField.PHYSICAL_EXAMINATION -> currentState.editingPatient[index].copy(physicalExamination = newValue as String)

            PatientField.DIAGNOSIS -> currentState.editingPatient[index].copy(diagnosis = newValue as String)
            PatientField.INVESTIGATION -> currentState.editingPatient[index].copy(investigation = newValue as String)

            PatientField.TREATMENT -> currentState.editingPatient[index].copy(treatment = newValue as String)
            PatientField.SUMMARY -> currentState.editingPatient[index].copy(summary = newValue as String)

            PatientField.UPDATE -> currentState.editingPatient[index].copy(patientUpdate = newValue as String)
        }
        val newEditingPatient = _state.value.editingPatient.toMutableList()
        newEditingPatient[index] = updatedPatient

        _state.value = _state.value.copy(editingPatient = newEditingPatient, isEditing = true)
    }


    private fun showError(message: String) {
        viewModelScope.launch {
            _state.value.snackbarHostState.showSnackbar(message)
        }
    }
}