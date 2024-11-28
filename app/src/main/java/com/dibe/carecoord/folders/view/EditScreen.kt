package com.dibe.carecoord.folders.view

import android.content.Context
import android.widget.DatePicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.dibe.carecoord.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.dibe.carecoord.folders.view.widgets.Header
import com.dibe.carecoord.folders.viewmodel.FolderEvent
import com.dibe.carecoord.folders.viewmodel.FolderState
import com.dibe.carecoord.folders.viewmodel.util.PatientField
import com.dibe.carecoord.folders.view.widgets.AlertDialog
import com.dibe.carecoord.folders.view.widgets.BloodPressureCard
import com.dibe.carecoord.folders.view.widgets.GenderOption
import com.dibe.carecoord.folders.view.widgets.MaritalStatusOption
import com.dibe.carecoord.old.Presentation.Screens.Screens
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(onEvent: (FolderEvent) -> Unit, state: FolderState, navController: NavController, context: Context) {
    var isOn by remember { mutableStateOf(false) }
    var isDeleteOn by remember { mutableStateOf(false) }
    var isSaveOn by remember { mutableStateOf(false) }
    var currentPageIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.edit_name)) } ,
                navigationIcon = {
                    IconButton(onClick = { isOn = ! isOn }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack , contentDescription = "the back")
                    }
                } ,
                actions = {
                    if (state.isEditing) {
                        IconButton(onClick = { isSaveOn = ! isSaveOn }) {
                            Icon(Icons.Default.Save , contentDescription = "save")
                        }
                    }
                    IconButton(onClick = { isDeleteOn = ! isDeleteOn }) {
                        Icon(Icons.Default.Delete , contentDescription = "delete")
                    }
                }
                     )
        } ,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screens.BODY_PARAMETERS.name)
                    onEvent(FolderEvent.CreateTimeline)
                          } ,
                icon = { Icon(imageVector = Icons.Default.Add , contentDescription = "Add Icon") } ,
                text = { Text("Add Timeline") }
                                        )
        }
            ) { paddingValues ->

        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { state.editingPatient.size }
                                           )
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.ime)
              ) {
            // TabRow for the tabs at the top
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth()
                  ) {
                // Create a tab for each page in editingPatient
                state.editingPatient.forEachIndexed { index , patient ->
                    Tab(
                        selected = pagerState.currentPage == index ,
                        onClick = {
                            // Scroll to the page when a tab is clicked
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        } ,
                        text = {
                            // Display the tab text, could be patient name or other data
                            Text(text = patient.name) // Adjust as per your data
                        }
                       )
                }
            }

            // Observe page index changes
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collectLatest { page ->
                    currentPageIndex = page // Update current page index
                }
            }

            // Display the pager with the pager state
            HorizontalPager(
                state = pagerState,
                           ) { page ->
                EditDisplay(state = state , onEvent = onEvent , index = page , context = context)
            }
        }


        if (isOn) {
            AlertDialog(
                onDismissRequest = { isOn = !isOn } ,
                onConfirmation = {
                    navController.navigate(Screens.HOME.name)
                    onEvent(FolderEvent.ResetEdit)
                    isOn = false
                } ,
                dismissText = "Cancel" ,
                confirmText = "Yes" ,
                dialogTitle = "Info" ,
                dialogText = "Are you sure you want to go back to home?"
                       )
        }
        if (isSaveOn) {
            AlertDialog(
                onDismissRequest = { isSaveOn = !isSaveOn } ,
                onConfirmation = {
                    val patient = state.currentlyEditing[currentPageIndex]
                    onEvent(patient.let { FolderEvent.Update(it , state.editingPatient[currentPageIndex]) })
                    isSaveOn = !isSaveOn
                } ,
                dismissText = "Cancel" ,
                confirmText = "Yes" ,
                dialogTitle = "Info" ,
                dialogText = "Are you sure you want to update the data?"
                       )
        }
        if (isDeleteOn) {
            AlertDialog(
                onDismissRequest = { isDeleteOn = !isDeleteOn } ,
                onConfirmation = {
                    navController.navigate(Screens.HOME.name)
                    onEvent(state.currentlyEditing[currentPageIndex].let { FolderEvent.Delete(it) })
                    onEvent(FolderEvent.ResetEdit)
                    isDeleteOn = false
                } ,
                dismissText = "Cancel" ,
                confirmText = "Yes" ,
                dialogTitle = "Info" ,
                dialogText = "Are you sure you want to delete?"
                       )
        }
        BackHandler(
            enabled = true,
            onBack = {
                isOn = !isOn
            }
                   )
    }
}

@Composable
fun EditDisplay(state : FolderState , onEvent : (FolderEvent) -> Unit , index : Int, context: Context) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
          ) {
        Text(formatTimestamp(state.editingPatient[index].timestamp), fontSize = 15.sp, fontWeight = FontWeight.Normal, modifier = Modifier.padding(10.dp))
        // Pass the selectedTimeline and index to each section
        BasicDetailsSection(
            state = state ,
            selectedIndex = index ,
            onEvent = onEvent
                           )
        BodyParametersSection(
            state = state ,
            selectedIndex = index ,
            onEvent = onEvent
                             )
        if (state.editingPatient[index].sex == "Male") {
            FemaleDetailsSection(
                state = state ,
                selectedIndex = index ,
                onEvent = onEvent ,
                context = context
                                )
        }
        DoctorsRemarkSection(
            state = state ,
            selectedIndex = index ,
            onEvent = onEvent
                            )
    }
}

fun formatTimestamp(timestamp: Long): String {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = timestamp }

    // Calculate differences
    val diffInMillis = now.timeInMillis - timestamp
    val daysDiff = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
    val monthsDiff = (now.get(Calendar.YEAR) - date.get(Calendar.YEAR)) * 12 + now.get(Calendar.MONTH) - date.get(Calendar.MONTH)

    return when {
        // 1. Within the last minute
        TimeUnit.MILLISECONDS.toMinutes(diffInMillis) < 1 -> "Just now"

        // 2. Within the last hour
        TimeUnit.MILLISECONDS.toHours(diffInMillis) < 1 -> "${TimeUnit.MILLISECONDS.toMinutes(diffInMillis)} minutes ago"

        // 3. Within the last day
        TimeUnit.MILLISECONDS.toDays(diffInMillis) < 1 -> {
            val hoursAgo = TimeUnit.MILLISECONDS.toHours(diffInMillis)
            if (hoursAgo == 1L) "An hour ago" else "$hoursAgo hours ago"
        }

        // 4. Yesterday
        daysDiff == 1 -> "Yesterday"

        // 5. This week
        daysDiff <= 7 -> "This week"

        // 6. This month
        now.get(Calendar.MONTH) == date.get(Calendar.MONTH) -> "This month"

        // 7. Last month
        monthsDiff == 1 -> "Last month"

        // 8. Within this year (in months)
        monthsDiff in 2..11 -> "$monthsDiff months ago"

        // 9. This year
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR) -> {
            val dateFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
            dateFormatter.format(date.time)
        }

        // 10. Within 10 years
        now.get(Calendar.YEAR) - date.get(Calendar.YEAR) in 1..10 -> {
            "${now.get(Calendar.YEAR) - date.get(Calendar.YEAR)} years ago"
        }

        // 11. More than 10 years ago - Full date and time
        else -> {
            val dateFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            dateFormatter.format(date.time)
        }
    }
}

@Composable
fun BasicDetailsSection(state: FolderState , onEvent: (FolderEvent) -> Unit, selectedIndex: Int) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
        Column(
            modifier = Modifier.padding(10.dp)
              ) {
            Header("Basic Details:")
            HorizontalDivider(thickness = 2.dp)
            Spacer(modifier = Modifier.height(20.dp))
            CreateEditField("Name:", state.editingPatient[selectedIndex].name, PatientField.NAME, onEvent = onEvent, selectedIndex = selectedIndex)
            CreateEditField("Age:", state.editingPatient[selectedIndex].age.toString(), PatientField.AGE, onEvent = onEvent, selectedIndex = selectedIndex)
            EditGenderSelectionCard(state = state, onEvent = onEvent, selectedIndex = selectedIndex)
            CreateEditField("Occupation:", state.editingPatient[selectedIndex].occupation, PatientField.OCCUPATION, onEvent = onEvent, selectedIndex = selectedIndex)
            EditMaritalStatusSelectionCard(state = state, onEvent = onEvent, selectedIndex = selectedIndex)
            CreateEditField("Address:", state.editingPatient[selectedIndex].address, PatientField.ADDRESS, onEvent = onEvent, selectedIndex = selectedIndex)
            CreateEditField("Tribe:", state.editingPatient[selectedIndex].tribe, PatientField.TRIBE, onEvent = onEvent, selectedIndex = selectedIndex)
        }
    }
}

@Composable
fun BodyParametersSection(state: FolderState , onEvent: (FolderEvent) -> Unit, selectedIndex: Int) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
        Column(
            modifier = Modifier.padding(10.dp)
              ) {
            Header("Body Parameters:")
            HorizontalDivider(thickness = 2.dp)
            Spacer(modifier = Modifier.height(20.dp))
            EditFloatInputCard("Height:" , state.editingPatient[selectedIndex].height.toString() , PatientField.HEIGHT , onEvent = onEvent, selectedIndex = selectedIndex)
            EditFloatInputCard("Weight:", state.editingPatient[selectedIndex].weight.toString(), PatientField.WEIGHT, onEvent = onEvent, selectedIndex = selectedIndex)
            EditFloatInputCard("BMI:", state.editingPatient[selectedIndex].bmi.toString(), PatientField.BMI, onEvent = onEvent, selectedIndex = selectedIndex)
            EditIntegerInputCard("Pulse:", state.editingPatient[selectedIndex].pulse.toString(), PatientField.PULSE, onEvent = onEvent, selectedIndex = selectedIndex)
            EditFloatInputCard("Oxygen Saturation:", state.editingPatient[selectedIndex].oxygenSaturation.toString(), PatientField.OXYGEN_SATURATION, onEvent = onEvent, selectedIndex = selectedIndex)
            BloodPressureCard(sysValue = state.editingPatient[selectedIndex].sys.toString() , onSysChange = { value: String -> onEvent(FolderEvent.ChangeEditValue(PatientField.SYS , value, index = selectedIndex)) } , state.editingPatient[selectedIndex].dys.toString() , onDysChange = { value: String -> onEvent(FolderEvent.ChangeEditValue(PatientField.DYS , value, index = selectedIndex)) } , isEdit = true)
        }
    }
}

@Composable
fun FemaleDetailsSection(state: FolderState , onEvent: (FolderEvent) -> Unit, context: Context, selectedIndex : Int) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
        Column(
            modifier = Modifier.padding(10.dp)
              ) {
            Header("Female Details:")
            HorizontalDivider(thickness = 2.dp)
            Spacer(modifier = Modifier.height(20.dp))
            EditLmpAndEddCard(onEvent = onEvent , state = state , context = context, selectedIndex = selectedIndex)
            CreateEditField("Gravidity:", state.editingPatient[selectedIndex].gravidity, PatientField.GRAVIDITY, onEvent = onEvent, selectedIndex = selectedIndex)
            CreateEditField("Parity:", state.editingPatient[selectedIndex].parity, PatientField.PARITY, onEvent = onEvent, selectedIndex = selectedIndex)
            CreateEditField("Katamenia:", state.editingPatient[selectedIndex].katamenia, PatientField.KATAMENIA, onEvent = onEvent, selectedIndex = selectedIndex)
        }
    }
}

@Composable
fun DoctorsRemarkSection(state: FolderState, onEvent: (FolderEvent) -> Unit, selectedIndex : Int) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        ) {
        Column(
            modifier = Modifier.padding(10.dp)
              ) {
            Header("Doctors Remark:")
            HorizontalDivider(thickness = 2.dp)
            Spacer(modifier = Modifier.height(20.dp))
            CreateEditField("Complain:", state.editingPatient[selectedIndex].complain, PatientField.COMPLAIN, onEvent = onEvent, selectedIndex = selectedIndex)
            CreateEditField("History Of Presenting Complain:", state.editingPatient[selectedIndex].hpc, PatientField.HPC, onEvent = onEvent, selectedIndex = selectedIndex)
            CreateEditField("Gynaecological History:", state.editingPatient[selectedIndex].gynaeHistory, PatientField.GYNAE_HISTORY, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Past Medical / Surgical History:", state.editingPatient[selectedIndex].medicalHistory, PatientField.MEDICAL_HISTORY, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Family / Social History:", state.editingPatient[selectedIndex].socialHistory, PatientField.SOCIAL_HISTORY, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Drug History:", state.editingPatient[selectedIndex].drugHistory, PatientField.DRUG_HISTORY, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Review of System:", state.editingPatient[selectedIndex].ros, PatientField.ROS, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Physical Examination:", state.editingPatient[selectedIndex].physicalExamination, PatientField.PHYSICAL_EXAMINATION, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Diagnosis:", state.editingPatient[selectedIndex].diagnosis, PatientField.DIAGNOSIS, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Investigation:", state.editingPatient[selectedIndex].investigation, PatientField.INVESTIGATION, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Treatment:", state.editingPatient[selectedIndex].treatment, PatientField.TREATMENT, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Summary:", state.editingPatient[selectedIndex].summary, PatientField.SUMMARY, onEvent, selectedIndex = selectedIndex)
            CreateEditField("Update:", state.editingPatient[selectedIndex].patientUpdate, PatientField.UPDATE, onEvent, selectedIndex = selectedIndex)
        }
    }
}

@Composable
fun CreateEditField(label: String, text: String, field: PatientField, onEvent: (FolderEvent) -> Unit, selectedIndex : Int) {
    var textState by remember { mutableStateOf(text) }
    Column(modifier = Modifier.padding(5.dp)) {
        Text(
            text = label,
            fontSize = 18.sp,
            )
        TextField(
            value = textState,
            onValueChange = {
                textState = it
                onEvent(FolderEvent.ChangeEditValue(field, it, selectedIndex))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                 )
    }
}

@Composable
fun EditFloatInputCard(
        label: String,
        initialValue: String,
        field: PatientField,
        onEvent: (FolderEvent) -> Unit,
        trailingIcon: String? = null,
        selectedIndex : Int
                      ) {
    // State management for main and decimal parts
    var mainPart by remember { mutableStateOf(initialValue.substringBefore(".")) }
    var decimalPart by remember { mutableStateOf(initialValue.substringAfter(".")) }

    // Function to handle combined float value updates
    fun updateValue() {
        val combinedValue = "$mainPart.$decimalPart".toFloatOrNull() ?: 0f
        onEvent(FolderEvent.ChangeEditValue(field, combinedValue.toString(), selectedIndex))
    }

    // Main card structure
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
        ) {
        Column {
            // Header label styled like CreateEditField
            Text(
                text = label,
                fontSize = 18.sp,
                )

            // Input row for main and decimal parts with optional trailing icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
               ) {
                // Main part TextField
                TextField(
                    value = if (mainPart == "0") "" else mainPart ,
                    onValueChange = {
                        mainPart = if (it.isEmpty()) "0" else it
                        updateValue()
                    } ,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) ,
                    maxLines = 1 ,
                    modifier = Modifier.weight(1f)                      )

                // Decimal point separator
                Text(
                    text = ".",
                    modifier = Modifier.padding(horizontal = 4.dp)
                    )

                // Decimal part TextField
                TextField(
                    value = if (decimalPart == "0") "" else decimalPart,
                    onValueChange = {
                        decimalPart = if (it.isEmpty()) "0" else it
                        updateValue()
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    modifier = Modifier.weight(1f)                       )

                // Optional trailing icon for units, if provided
                trailingIcon?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(start = 8.dp)
                        )
                }
            }
        }
    }
}

@Composable
fun EditIntegerInputCard(
        label: String,
        initialValue: String,
        field: PatientField,
        onEvent: (FolderEvent) -> Unit,
        trailingIcon: String? = null,
        selectedIndex : Int
                        ) {
    // State management for the integer input
    var intState by remember { mutableStateOf(initialValue) }

    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
        ) {
        Column {
            // Header label styled like CreateEditField
            Text(
                text = label,
                fontSize = 18.sp,
                )

            // Integer input field with optional trailing icon
            TextField(
                value = if (intState == "0") "" else intState,
                onValueChange = {
                    val newValue = it.filter { char -> char.isDigit() }
                    intState = if (newValue.isEmpty()) "0" else newValue
                    onEvent(FolderEvent.ChangeEditValue(field, intState, selectedIndex))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                trailingIcon = trailingIcon?.let { { Text(it) } }
                     )
        }
    }
}

@Composable
fun EditLmpAndEddCard(
        onEvent: (FolderEvent) -> Unit,
        state: FolderState,
        context: Context,
        selectedIndex : Int
                     ) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
        ) {
        Column (
            modifier = Modifier.padding(bottom = 10.dp)
               ){
            // Calendar setup for date pickers
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // LMP DatePickerDialog
            val lmpDatePickerDialog = android.app.DatePickerDialog(
                context ,
                { _ : DatePicker , selectedYear : Int , selectedMonth : Int , selectedDay : Int ->
                    onEvent(
                        FolderEvent.ChangeEditValue(
                            PatientField.LMP ,
                            "$selectedDay/${selectedMonth + 1}/$selectedYear",
                            selectedIndex
                                                   )
                           )
                } ,
                year , month , day
                                                                  )

            // EDD DatePickerDialog
            val eddDatePickerDialog = android.app.DatePickerDialog(
                context ,
                { _ : DatePicker , selectedYear : Int , selectedMonth : Int , selectedDay : Int ->
                    onEvent(
                        FolderEvent.ChangeEditValue(
                            PatientField.EDD ,
                            "$selectedDay/${selectedMonth + 1}/$selectedYear",
                            selectedIndex
                                                   )
                           )
                } ,
                year , month , day
                                                                  )

            // LMP Field
            Text(
                text = "LMP:",
                fontSize = 18.sp,
                )
            TextField(
                value = state.editingPatient[selectedIndex].lmp,
                readOnly = true,
                onValueChange = {

                },
                trailingIcon = {
                    IconButton(onClick = { lmpDatePickerDialog.show() }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select LMP Date")
                    }
                },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
                     )

            Spacer(modifier = Modifier.height(20.dp))

            // EDD Field
            Text(
                text = "EDD:",
                fontSize = 18.sp,
                )
            TextField(
                value = state.editingPatient[selectedIndex].edd,
                readOnly = true,
                onValueChange = { },
                trailingIcon = {
                    IconButton(onClick = { eddDatePickerDialog.show() }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select EDD Date")
                    }
                },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
                     )
        }
    }
}

@Composable
fun EditGenderSelectionCard(state: FolderState , onEvent: (FolderEvent) -> Unit, selectedIndex : Int) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
        ) {
        Column{
            Text(
                text = "Sex:",
                fontSize = 18.sp,
                )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
               ) {
                GenderOption(
                    text = "Male",
                    isSelected = state.editingPatient[selectedIndex].sex == "Male",
                    onValueChange = { onEvent(FolderEvent.ChangeEditValue(PatientField.SEX , "Male", selectedIndex)) }
                            )
                GenderOption(
                    text = "Female",
                    isSelected = state.editingPatient[selectedIndex].sex == "Female",
                    onValueChange = { onEvent(FolderEvent.ChangeEditValue(PatientField.SEX , "Female", selectedIndex)) }
                            )
            }
        }
    }
}

@Composable
fun EditMaritalStatusSelectionCard(state: FolderState , onEvent: (FolderEvent) -> Unit, selectedIndex : Int) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
        ) {
        Column {
            Text(
                text = "Marital Status:",
                fontSize = 18.sp,
                )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
               ) {
                MaritalStatusOption(
                    text = "M",
                    state = state,
                    isSelected = state.editingPatient[selectedIndex].maritalStatus == "M",
                    onValueChange = { onEvent(FolderEvent.ChangeEditValue(PatientField.MARITAL_STATUS, "M", selectedIndex)) }
                                   )
                MaritalStatusOption(
                    text = "S",
                    state = state,
                    isSelected = state.editingPatient[selectedIndex].maritalStatus == "S",
                    onValueChange = { onEvent(FolderEvent.ChangeEditValue(PatientField.MARITAL_STATUS, "S", selectedIndex)) }
                                   )
                MaritalStatusOption(
                    text = "D",
                    state = state,
                    isSelected = state.editingPatient[selectedIndex].maritalStatus == "D",
                    onValueChange = { onEvent(FolderEvent.ChangeEditValue(PatientField.MARITAL_STATUS, "D", selectedIndex)) }
                                   )
            }
        }
    }
}

@Composable
fun HorizontalDivider(thickness: Dp) {
    HorizontalDivider(modifier = Modifier.height(thickness) , color = Color.Gray)
}

