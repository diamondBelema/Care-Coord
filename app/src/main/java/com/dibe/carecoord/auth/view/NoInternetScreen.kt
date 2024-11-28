package com.dibe.carecoord.old.Presentation.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.dibe.carecoord.R // Adjust the package name according to your project

@Composable
fun NoInternetConnectionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
          ) {
        Image(
            painter = painterResource(id = R.drawable.ic_wifi_off), // Use your own drawable resource
            contentDescription = "No Internet Connection",
            modifier = Modifier.size(100.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Red)
             )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Oops! There is no internet connection.",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
            )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please check your connection.",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
            )
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
    return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

