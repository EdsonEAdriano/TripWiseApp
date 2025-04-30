package com.example.tripwiseapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun MainScreen(
    onNavigateTo:(String) -> Unit
){
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "List of Trips",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            ListTripsScreen(
                onInsertOrEdit = {
                    if (it == null)
                        onNavigateTo("TripScreen")
                    else
                        onNavigateTo("TripScreen/${it}")
                }
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewMainScreen() {
    TripWiseAppTheme {
        MainScreen({})
    }
}