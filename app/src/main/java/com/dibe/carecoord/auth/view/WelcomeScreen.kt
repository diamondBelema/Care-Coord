package com.dibe.carecoord.auth.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dibe.carecoord.R

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
       ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally ,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
              ) {
            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(id = R.drawable.logo) ,
                contentDescription = "App Logo" ,
                modifier = Modifier.size(100.dp)
                 )

            Text(
                text = "Enjoy Seamless Folder Creation" ,
                fontSize = 24.sp ,
                 textAlign = TextAlign.Center ,
                modifier = Modifier.padding(vertical = 16.dp)
                )

            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sagittis enim purus sed pharetra.",
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
                )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("") } ,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)) ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                  ) {
                Text("Get Started", color = Color.White)
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun WelcomeScreenPreview(){
    WelcomeScreen(navController = rememberNavController())
}

