package com.dibe.carecoord.auth.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dibe.carecoord.auth.viewmodel.AuthEvent
import com.dibe.carecoord.old.Presentation.Screens.NoInternetConnectionScreen
import com.dibe.carecoord.old.Presentation.Screens.Screens
import com.dibe.carecoord.old.Presentation.Screens.isNetworkAvailable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
        onEvent: (AuthEvent) -> Unit,
        context: Context,
        navController: NavController
               ) {
    val isNetworkAvailable = remember { mutableStateOf(isNetworkAvailable(context)) }

    if (!isNetworkAvailable.value) {
        NoInternetConnectionScreen()
    } else {
        Scaffold(
            topBar = {
                MediumTopAppBar(
                    title = { Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append("L")
                        }
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("ogin")
                        }
                    }, fontSize = 30.sp) } ,
                        )
            },
            content = { padding ->
                LoginContent(
                    modifier = Modifier.padding(padding),
                    onEvent = onEvent,
                    navController = navController
                            )
            },
            bottomBar = {

            }
                )
    }
}

@Composable
fun LoginContent(
        modifier: Modifier = Modifier,
        onEvent: (AuthEvent) -> Unit,
        navController: NavController,
        borderShape: Shape = RoundedCornerShape(8.dp)
                ) {
    // Initialize state for input fields and error messages
    val (email, setEmail) = remember { mutableStateOf(TextFieldValue()) }
    val (emailErrorState, setEmailErrorState) = remember { mutableStateOf(false) }

    val (password, setPassword) = remember { mutableStateOf(TextFieldValue()) }
    val (passwordErrorState, setPasswordErrorState) = remember { mutableStateOf(false) }

    val (passwordVisibility, setPasswordVisibility) = remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
          ) {

        // Bordered container for form fields
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
              ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {
                    setEmailErrorState(false)
                    setEmail(it)
                                },
                label = {
                    Text(text = "Enter Email*")
                        } ,
                isError = emailErrorState,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                             )
            if (emailErrorState) ErrorText("Required")

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {
                    setPasswordErrorState(false)
                    setPassword(it)
                                },
                label = {
                    Text(text = "Enter Password*")
                        } ,
                isError = passwordErrorState,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    IconButton(onClick = { setPasswordVisibility(!passwordVisibility) }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle Password Visibility",
                            tint = MaterialTheme.colorScheme.error
                            )
                    }
                }
                             )
            if (passwordErrorState) ErrorText("Required")
            Spacer(Modifier.size(16.dp))
            // Login button
            Button(
                onClick = {
                    val hasErrors = validateFields(email, password, setEmailErrorState, setPasswordErrorState)
                    if (!hasErrors) {
                        onEvent(
                            AuthEvent.LoginUser(
                                email = email.text,
                                password = password.text
                                               ){
                                navController.navigate(Screens.HOME.name)
                            }
                               )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                  ) {
                Text(text = "Login", color = Color.White)
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
                ){
                Text("Don't have an account?")
                TextButton(onClick = { navController.navigate(Screens.REGISTRATION.name) }) {
                    Text(text = "Sign Up", color = MaterialTheme.colorScheme.error)
                }
             }
        }
    }
}


// Helper function to display an error message
@Composable
private fun ErrorText(text: String) {
    Text(text = text, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
}

// Validation function to handle error states
private fun validateFields(
        email: TextFieldValue,
        password: TextFieldValue,
        setEmailError: (Boolean) -> Unit,
        setPasswordError: (Boolean) -> Unit,
                          ): Boolean {
    var hasErrors = false
    if (email.text.isEmpty()) {
        setEmailError(true)
        hasErrors = true
    }
    if (password.text.isEmpty()) {
        setPasswordError(true)
        hasErrors = true
    }
    return hasErrors
}
