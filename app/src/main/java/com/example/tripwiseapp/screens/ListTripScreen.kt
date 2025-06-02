package com.example.tripwiseapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import com.example.tripwiseapp.database.AppDatabase
import com.example.tripwiseapp.entity.Trip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tripwiseapp.api.getSuggestion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ListTripsScreen(onInsertOrEdit: (Int?) -> Unit) {
    val context = LocalContext.current
    val tripDao = AppDatabase.getDatabase(context).tripDao()

    var trips by remember { mutableStateOf(emptyList<Trip>()) }
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }

    var selectedTripId by remember { mutableStateOf<Int?>(null) }

    var isSuggestionDialogOpen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var aiSuggestion by remember { mutableStateOf("") }


    fun GetAISuggestion(destiny: String, startDate: String, endDate: String){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val suggestion = getSuggestion(destiny, startDate, endDate);

                withContext(Dispatchers.Main) {
                    aiSuggestion = suggestion
                    isLoading = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    aiSuggestion = "Error while generating suggestion. ERROR MESSAGE: ${e.message}"
                    isLoading = false
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        trips = tripDao.getAllTrips().sortedByDescending { it.id }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        trips.forEach { trip ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),

                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                        modifier = Modifier.padding(16.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    onInsertOrEdit(trip.id)
                                }
                            )
                        }
                ) {
                    Text("Destiny: ${trip.destiny}", style = MaterialTheme.typography.bodyLarge)
                    Text("Type: ${trip.type}", style = MaterialTheme.typography.bodyMedium)
                    Text("Start: ${formatDate(trip.startDate.toString())}", style = MaterialTheme.typography.bodyMedium)
                    Text("End: ${formatDate(trip.endDate.toString())}", style = MaterialTheme.typography.bodyMedium)
                    Text("Budget: R$ ${"%.2f".format(trip.budget)}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { tripToDelete = trip },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove",
                                modifier = Modifier.size(25.dp).padding(end = 3.dp)
                            )
                            Text("Remove")
                        }

                        Button(
                            onClick = {
                                GetAISuggestion(trip.destiny, trip.startDate.toString(), trip.endDate.toString())
                                isSuggestionDialogOpen = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = "Suggestion",
                                modifier = Modifier.size(25.dp).padding(end = 3.dp)
                            )
                            Text("Suggestion")
                        }
                    }
                }
            }
        }
    }


    if (tripToDelete != null) {
        AlertDialog(
            onDismissRequest = { tripToDelete = null },
            title = { Text("Confirm Remove") },
            text = { Text("Are you sure you want to remove this trip?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            tripToDelete?.let { trip ->
                                tripDao.deleteTrip(trip.id)
                                trips = tripDao.getAllTrips().sortedBy { Date(it.startDate.toString()) }
                                tripToDelete = null
                            }
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { tripToDelete = null }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (isSuggestionDialogOpen) {
        AlertDialog(
            onDismissRequest = { isSuggestionDialogOpen = false },
            confirmButton = {
                TextButton(onClick = { isSuggestionDialogOpen = false }) {
                    Text("Close")
                }
            },
            title = {
                Text("Destiny Suggestions")
            },
            text = {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Generating Suggestions...")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(aiSuggestion)
                    }
                }
            }
        )
    }

}




fun formatDate(dateString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = parser.parse(dateString)
        date?.let { formatter.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}