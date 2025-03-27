package com.example.oamkhub.presentation.ui.screen.front

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.oamkhub.R
import com.example.oamkhub.presentation.ui.theme.PrimaryOrange

@Composable
fun FrontScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.network_people),
            contentDescription = "Community Network",
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Your Campus, Your Hub",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Everything You need in one place",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                listOf(
                    "MarketPlace",
                    "Lost & Found",
                    "Events & Calendars",
                    "News & Announcements"
                ).forEach {
                    Text("• $it", fontSize = 14.sp)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(140.dp)
            ) {
                Text("Login", color = Color.White)
            }

            OutlinedButton(
                onClick = { navController.navigate("signup") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(140.dp)
            ) {
                Text("Register")
            }
        }
    }
}
