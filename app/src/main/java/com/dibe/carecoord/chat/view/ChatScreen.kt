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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dibe.carecoord.R
import com.dibe.carecoord.chat.viewmodel.ChatEvent
import com.dibe.carecoord.chat.viewmodel.ChatState
import com.dibe.carecoord.chat.viewmodel.util.Chat
import com.dibe.carecoord.old.Presentation.Screens.BottomNavigationBar
import com.dibe.carecoord.old.Presentation.Screens.NoInternetConnectionScreen
import com.dibe.carecoord.old.Presentation.Screens.isNetworkAvailable
import com.dibe.carecoord.utilities.SharedPreferencesHelper
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(context : Context , state: ChatState , onEvent: (ChatEvent) -> Unit , navController: NavController) {
    var message by remember { mutableStateOf(TextFieldValue("")) }
    Scaffold(
        bottomBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                   ) {
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Send message") }
                             )
                    IconButton(
                        onClick = {
                            SharedPreferencesHelper.getUserID()?.let {
                                SharedPreferencesHelper.getUsername()?.let { it1 ->
                                    SharedPreferencesHelper.getHospitalID()?.let { it2 ->
                                        Chat(
                                            senderId = it ,
                                            senderName = it1 ,
                                            hospitalId = it2 ,
                                            message = message.text ,
                                            timestamp = System.currentTimeMillis()
                                            )
                                    }
                                }
                            }?.let {
                                ChatEvent.SendMessage(
                                    it
                                                     )
                            }?.let {
                                onEvent(
                                    it
                                       )
                            }
                            message = TextFieldValue("")
                        }
                                   ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "send icon")
                    }
                }
                BottomNavigationBar(navController = navController)
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        ){
                        Image(painter = painterResource(id = R.drawable.logo) , contentDescription = "Logo" , modifier = Modifier.size(25.dp))
                        Text(text = "Chat" , modifier = Modifier.absolutePadding(left = 10.dp))
                    }
                },
                     )
        }
            ) { paddingValues ->
        val isNetworkAvailable = remember { mutableStateOf(isNetworkAvailable(context)) }
        if (!isNetworkAvailable.value) {
            NoInternetConnectionScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                      ) {
                items(state.chats.size) { index ->
                    ChatMessageItem(state.chats[index])
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(chatMessage: Chat) {
    val isCurrentUser = SharedPreferencesHelper.getUserID() == chatMessage.senderId

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
          ) {
        Card(
            shape = RoundedCornerShape(8.dp) ,
            colors = CardDefaults.cardColors(containerColor = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary) ,
            modifier = Modifier
                .widthIn(max = 300.dp)
                .padding(horizontal = 8.dp)
            ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (! isCurrentUser) {
                    Text(
                        text = chatMessage.senderName ,
                        style = MaterialTheme.typography.bodyMedium ,
                        color = MaterialTheme.colorScheme.onSecondary
                        )
                }
                Text(
                    text = chatMessage.message ,
                    style = MaterialTheme.typography.bodyLarge ,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                    )
            }
        }
        Text(
            text = formatTimestamp(chatMessage.timestamp.toLong()) ,
            style = MaterialTheme.typography.bodySmall ,
            modifier = Modifier.padding(horizontal = 16.dp , vertical = 4.dp)
            )
    }
}

fun formatTimestamp(timestamp: Long): String {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = timestamp }

    // Calculate differences
    val diffInMillis = now.timeInMillis - timestamp
    if (diffInMillis < 0) return "In the future"

    val minutesDiff = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val hoursDiff = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val daysDiff = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
    val monthsDiff = (now.get(Calendar.YEAR) - date.get(Calendar.YEAR)) * 12 +
            now.get(Calendar.MONTH) - date.get(Calendar.MONTH)
    val yearsDiff = now.get(Calendar.YEAR) - date.get(Calendar.YEAR)

    return when {
        // 1. Within the last minute
        minutesDiff < 1 -> "Just now"

        // 2. Within the last hour
        minutesDiff < 60 -> "$minutesDiff minutes ago"

        // 3. Within the last day
        hoursDiff < 24 -> {
            if (hoursDiff == 1L) "An hour ago" else "$hoursDiff hours ago"
        }

        // 4. Yesterday
        daysDiff == 1 -> "Yesterday"

        // 5. This week
        now.get(Calendar.WEEK_OF_YEAR) == date.get(Calendar.WEEK_OF_YEAR) &&
                now.get(Calendar.YEAR) == date.get(Calendar.YEAR) -> "This week"

        // 6. This month
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == date.get(Calendar.MONTH) -> "This month"

        // 7. Last month
        monthsDiff == 1 -> "Last month"

        // 8. Within this year (in months)
        monthsDiff in 2..11 -> "$monthsDiff months ago"

        // 9. This year
        yearsDiff == 0 -> {
            val dateFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
            dateFormatter.format(date.time)
        }

        // 10. Within 10 years
        yearsDiff in 1..10 -> "$yearsDiff years ago"

        // 11. More than 10 years ago
        else -> {
            val dateFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            dateFormatter.format(date.time)
        }
    }
}

