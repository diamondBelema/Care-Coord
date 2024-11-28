package com.dibe.carecoord.auth.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dibe.carecoord.auth.viewmodel.AuthEvent
import com.dibe.carecoord.old.Presentation.Screens.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(onEvent: (AuthEvent) -> Unit, navController: NavController) {
    val scrollState = rememberScrollState()

    val name = remember { mutableStateOf(TextFieldValue()) }
    val email = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue()) }
    val hospitalID = remember { mutableStateOf(TextFieldValue()) }
    val bio = remember { mutableStateOf(TextFieldValue()) }
    val occupation = remember { mutableStateOf(TextFieldValue()) }

    val nameErrorState = remember { mutableStateOf(false) }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val confirmPasswordErrorState = remember { mutableStateOf(false) }
    val hospitalIDErrorState = remember { mutableStateOf(false) }

    val passwordVisibility = remember { mutableStateOf(true) }
    val confirmPasswordVisibility = remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) { append("R") }
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) { append("egistration") }
                    }, fontSize = 30.sp)
                }
                           )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
                  ) {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {
                        nameErrorState.value = false
                        name.value = it
                    },
                    label = { Text(text = "Name*") },
                    isError = nameErrorState.value,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                                 )
                if (nameErrorState.value) ErrorText("Required")

                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        emailErrorState.value = false
                        email.value = it
                    },
                    label = { Text(text = "Email*") },
                    isError = emailErrorState.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                                 )
                if (emailErrorState.value) ErrorText("Required")

                OutlinedTextField(
                    value = password.value,
                    onValueChange = {
                        passwordErrorState.value = false
                        password.value = it
                    },
                    label = { Text(text = "Password*") },
                    isError = passwordErrorState.value,
                    visualTransformation = if (passwordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                            Icon(
                                imageVector = if (passwordVisibility.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                                )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                                 )
                if (passwordErrorState.value) ErrorText("Required")

                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = {
                        confirmPasswordErrorState.value = false
                        confirmPassword.value = it
                    },
                    label = { Text(text = "Confirm Password*") },
                    isError = confirmPasswordErrorState.value,
                    visualTransformation = if (confirmPasswordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisibility.value = !confirmPasswordVisibility.value }) {
                            Icon(
                                imageVector = if (confirmPasswordVisibility.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                                )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                                 )
                if (confirmPasswordErrorState.value) ErrorText("Passwords do not match")

                OutlinedTextField(
                    value = hospitalID.value,
                    onValueChange = {
                        hospitalIDErrorState.value = false
                        hospitalID.value = it
                    },
                    label = { Text(text = "Hospital ID*") },
                    isError = hospitalIDErrorState.value,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                                 )
                if (hospitalIDErrorState.value) ErrorText("Required")

                OutlinedTextField(
                    value = occupation.value,
                    onValueChange = {
                        occupation.value = it
                    },
                    label = { Text(text = "Occupation") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                                 )

                OutlinedTextField(
                    value = bio.value,
                    onValueChange = {
                        bio.value = it
                    },
                    maxLines = 10,
                    singleLine = false,
                    label = { Text(text = "Bio") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                                 )

                Text("Note: The hospital ID is a unique ID for every hospital known only to the hospital staffs so it must be kept an absolute secret", fontSize = 10.sp, fontWeight = FontWeight.Light)

                Button(
                    onClick = {
                        val hasErrors = validateFields(
                            name.value, email.value, password.value, confirmPassword.value, hospitalID.value,
                            nameErrorState, emailErrorState, passwordErrorState, confirmPasswordErrorState, hospitalIDErrorState
                                                      )
                        if (!hasErrors) {
                            onEvent(
                                AuthEvent.CreateUser(
                                    email = email.value.text,
                                    name = name.value.text,
                                    password = password.value.text,
                                    hospitalID = hospitalID.value.text,
                                    bio = bio.value.text,
                                    occupation = occupation.value.text,
                                                    ){
                                    navController.navigate(Screens.LOGIN.name)
                                }
                                   )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                      ) {
                    Text(text = "Register", color = Color.White)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp) ,
                    verticalAlignment = Alignment.CenterVertically ,
                    horizontalArrangement = Arrangement.Center
                   ) {
                    Text("Already have an account?")
                    TextButton(onClick = { navController.navigateUp() }) {
                        Text(text = "Login", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        },
        bottomBar = {

        }
            )
}

@Composable
private fun ErrorText(text: String) {
    Text(text = text, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
}

private fun validateFields(
        name: TextFieldValue,
        email: TextFieldValue,
        password: TextFieldValue,
        confirmPassword: TextFieldValue,
        hospitalID: TextFieldValue,
        nameErrorState: MutableState<Boolean>,
        emailErrorState: MutableState<Boolean>,
        passwordErrorState: MutableState<Boolean>,
        confirmPasswordErrorState: MutableState<Boolean>,
        hospitalIDErrorState: MutableState<Boolean>
                          ): Boolean {
    var hasErrors = false
    if (name.text.isEmpty()) { nameErrorState.value = true; hasErrors = true }
    if (email.text.isEmpty()) { emailErrorState.value = true; hasErrors = true }
    if (password.text.isEmpty()) { passwordErrorState.value = true; hasErrors = true }
    if (confirmPassword.text.isEmpty() || confirmPassword.text != password.text) {
        confirmPasswordErrorState.value = true; hasErrors = true
    }
    if (hospitalID.text.isEmpty()) { hospitalIDErrorState.value = true; hasErrors = true }
    return hasErrors
}
