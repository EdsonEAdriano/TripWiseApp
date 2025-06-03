package com.example.tripwiseapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripwiseapp.components.ErrorDialog
import com.example.tripwiseapp.components.MyPasswordField
import com.example.tripwiseapp.components.MyTextField
import com.example.tripwiseapp.models.UserViewModel
import com.example.tripwiseapp.ui.theme.TripWiseAppTheme

@Composable
fun AboutScreen(){
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "About",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "This system was designed to simplify travel management by allowing users to easily register, view, and organize their trips. Through a clean and intuitive interface, you can enter details such as destination, travel dates, type of trip, and budget—all in one centralized place.\n" +
                        "\n" +
                        "What sets our system apart is the integration of Artificial Intelligence, which generates personalized suggestions for activities, itineraries, and tourist attractions based on the destination and travel dates provided. This allows you to enhance your planning process and make the most of every trip.\n" +
                        "\n" +
                        "Our mission is to combine technology and convenience to transform the way people plan their travels—offering not just a management tool, but a smart travel planning assistant.\n",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewAboutScreen() {
    TripWiseAppTheme {
        AboutScreen()
    }
}