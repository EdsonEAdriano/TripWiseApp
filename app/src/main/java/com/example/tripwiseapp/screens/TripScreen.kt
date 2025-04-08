package com.example.tripwiseapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripwiseapp.components.ErrorDialog
import com.example.tripwiseapp.components.MyDatePicker
import com.example.tripwiseapp.components.MyNumberField
import com.example.tripwiseapp.components.MyRatioGroupField
import com.example.tripwiseapp.components.MyTextField
import com.example.tripwiseapp.database.AppDatabase
import com.example.tripwiseapp.models.RegisterTrip
import com.example.tripwiseapp.models.TripViewModel
import com.example.tripwiseapp.models.TripViewModelFactory
import com.example.tripwiseapp.ui.theme.TripWiseAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripScreen(
    onNavigateTo:(String) -> Unit
){
    val ctx = LocalContext.current

    val tripDao = AppDatabase.getDatabase(ctx).tripDao()
    val tripViewModel : TripViewModel = viewModel(
        factory = TripViewModelFactory(tripDao)
    )

    val registerTrip = tripViewModel.uiState.collectAsState()

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "Trip",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(30.dp)
            )


            MyTextField(
                label = "Destiny",
                value = registerTrip.value.destiny,
                onValueChange = {
                    tripViewModel.onDestinyChange(it)
                },
            )
            MyRatioGroupField(
                label = "Type",
                value = registerTrip.value.type,
                options = listOf("Leisure ", "Work"),
                onValueChange = {
                    tripViewModel.onTypeChange(it)
                }
            )

            MyDatePicker(
                label = "Start Date",
                value = registerTrip.value.start,
                onValueChange = {
                    tripViewModel.onStartChange(it)
                }
            )

            MyDatePicker(
                label = "End Date",
                value = registerTrip.value.end,
                onValueChange = {
                    tripViewModel.onEndChange(it)
                }
            )

            MyNumberField(
                label = "Budget",
                value = registerTrip.value.budget.toString(),
                onValueChange = {
                    tripViewModel.onBudgetChange(it.toDouble())
                }
            )

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    tripViewModel.register()
                }
            ) {
                Text(text = "Save")
            }





            if (registerTrip.value.errorMessage.isNotBlank()) {
                ErrorDialog(
                    error = registerTrip.value.errorMessage,
                    onDismissRequest =  {
                        tripViewModel.cleanErrorMessage()
                    }
                )
            }
        }

        LaunchedEffect(registerTrip.value.isSaved) {
            if(registerTrip.value.isSaved){
                Toast.makeText(ctx, "Trip saved",
                    Toast.LENGTH_SHORT).show()

                onNavigateTo("MainScreen")
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewTripScreen() {
    TripWiseAppTheme {
        TripScreen({})
    }
}