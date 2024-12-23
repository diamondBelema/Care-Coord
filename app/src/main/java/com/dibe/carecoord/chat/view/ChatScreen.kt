package com.dibe.carecoord.chat.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dibe.carecoord.R
import com.dibe.carecoord.chat.viewmodel.ChatState
import com.dibe.carecoord.old.Presentation.Screens.BottomNavigationBar
import com.dibe.carecoord.old.Presentation.Screens.NoInternetConnectionScreen
import com.dibe.carecoord.old.Presentation.Screens.isNetworkAvailable
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ChatScreen(context : Context , state: ChatState , onEvent: (ChatEvent) -> Unit , navController: NavController) {
//    var message by remember { mutableStateOf(TextFieldValue("")) }
//    val coroutineScope = rememberCoroutineScope()
//    Scaffold(
//        bottomBar = {
//            Column {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                   ) {
//                    TextField(
//                        value = message,
//                        onValueChange = { message = it },
//                        modifier = Modifier.weight(1f),
//                        placeholder = { Text("Send message") }
//                             )
//                    IconButton(onClick = {
//                        onEvent(
//                            ChatEvent.SendMessage(
//                                message.text,
//                                onSuccess = {
//
//                                }
//                                                ){
//                                coroutineScope.launch {
//                                    state.snackbarHostState.showSnackbar(
//                                        message = "The message could not be sent",
//                                        actionLabel = "Dismiss"
//                                                                        )
//                                }
//                            })
//                        message = TextFieldValue("")  // Clear the text field after sending
//                    }) {
//                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "send icon")
//                    }
//                }
//                BottomNavigationBar(navController = navController)
//            }
//        },
//        topBar = {
//            TopAppBar(
//                title = {
//                    Row (
//                        verticalAlignment = Alignment.CenterVertically,
//                        ){
//                        Image(painter = painterResource(id = R.drawable.logo) , contentDescription = "Logo" , modifier = Modifier.size(25.dp))
//                        Text(text = "Chat" , modifier = Modifier.absolutePadding(left = 10.dp))
//                    }
//                },
//                     )
//        }
//            ) { paddingValues ->
//        val isNetworkAvailable = remember { mutableStateOf(isNetworkAvailable(context)) }
//        if (!isNetworkAvailable.value) {
//            NoInternetConnectionScreen()
//        } else {
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(paddingValues)
//                      ) {
//                items(state.chats.size) { index ->
//                    ChatMessageItem(state.chats[index])
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ChatMessageItem(chatMessage: Chat) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
//          ) {
//        Card(
//            shape = RoundedCornerShape(8.dp),
//            colors = CardDefaults.cardColors(containerColor = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary),
//            modifier = Modifier
//                .widthIn(max = 300.dp)
//                .padding(horizontal = 8.dp)
//            ) {
//            Column(modifier = Modifier.padding(12.dp)) {
//                if (!isCurrentUser) {
//                    Text(
//                        text = chatMessage.senderName ,
//                        style = MaterialTheme.typography.bodyMedium ,
//                        color = MaterialTheme.colorScheme.onSecondary
//                        )
//                }
//                Text(
//                    text = chatMessage.message ,
//                    style = MaterialTheme.typography.bodyLarge ,
//                    color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
//                    )
//            }
//        }
//        Text(
//            text = formatTimestamp(chatMessage.timestamp.toLong()) ,
//            style = MaterialTheme.typography.bodySmall ,
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
//            )
//    }
//}
//
//
//fun formatTimestamp(timestamp: Long): String {
//    val currentTime = System.currentTimeMillis()
//    val difference = currentTime - timestamp
//
//    val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)
//    val hours = TimeUnit.MILLISECONDS.toHours(difference)
//    val days = TimeUnit.MILLISECONDS.toDays(difference)
//
//    return when {
//        minutes < 1 -> "Just now"
//        minutes < 60 -> "$minutes minutes ago"
//        hours < 24 -> "$hours ${if (hours == 1L) "hour" else "hours"} ago"
//        days == 1L -> "Yesterday"
//        days < 7 -> "$days day ago"
//        else -> {
//            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//            sdf.format(Date(timestamp))
//        }
//    }
//}
//
