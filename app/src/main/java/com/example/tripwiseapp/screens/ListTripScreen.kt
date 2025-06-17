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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Card
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tripwiseapp.api.UpdateSuggestion
import com.example.tripwiseapp.api.getSuggestion
import com.example.tripwiseapp.entity.Suggestion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ListTripsScreen(onInsertOrEdit: (Int?) -> Unit, userId: MutableState<Long>) {
    val context = LocalContext.current
    val tripDao = AppDatabase.getDatabase(context).tripDao()

    val suggestionDao = AppDatabase.getDatabase(context).suggestionDao()

    var trips by remember { mutableStateOf(emptyList<Trip>()) }
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }

    var tripToSuggest by remember { mutableStateOf<Trip?>(null) }

    var isSuggestionDialogOpen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var suggestionId by remember { mutableLongStateOf(0) }
    var aiSuggestion by remember { mutableStateOf("") }

    var promptText by remember { mutableStateOf("") }


    suspend fun getSuggestionByTripId(tripId: Int?): Suggestion? {
        return withContext(Dispatchers.IO) {
            suggestionDao.findByTripId(tripId)
        }
    }


    fun GetAISuggestion(trip: Trip){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existing = getSuggestionByTripId(trip.id)

                var suggestionText: String;

                if (existing?.suggestion != null) {
                    suggestionId = existing.id
                    suggestionText = existing.suggestion
                } else {
                    val newText = getSuggestion(
                        trip.destiny,
                        trip.startDate.toString(),
                        trip.endDate.toString(),
                        trip.budget,
                        trip.type
                    )
                    val suggestion = Suggestion(tripId = trip.id, suggestion = newText)
                    suggestionId = suggestionDao.insertSuggestion(suggestion)
                    suggestionText = newText
                }

                withContext(Dispatchers.Main) {
                    aiSuggestion = suggestionText
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
        trips = tripDao.getAllTripsByUser(userId.value).sortedByDescending { it.id }
    }




    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(trips.size, key = { trips[it].id }) { index ->
            val trip = trips[index]

            val dismissState = rememberDismissState(
                confirmStateChange = { value ->
                    if (value == DismissValue.DismissedToEnd) {
                        tripToDelete = trip // Armazena para confirmar
                        false // Não remove ainda!
                    } else false
                }
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.StartToEnd),
                background = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text("Swipe to delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissContent = {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
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
                                    onClick = {
                                        tripToSuggest = trip
                                        aiSuggestion = ""
                                        isSuggestionDialogOpen = true
                                        isLoading = true
                                        GetAISuggestion(trip)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoFixHigh,
                                        contentDescription = "Suggestion",
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 3.dp)
                                    )
                                    Text("Suggestion")
                                }
                            }
                        }
                    }
                }
            )
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
                                trips = tripDao.getAllTripsByUser(userId.value).sortedBy { Date(it.startDate.toString()) }
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
            onDismissRequest = {
                isSuggestionDialogOpen = false
                tripToSuggest = null
                isLoading = false
                promptText = ""
            },
            confirmButton = {
                TextButton(onClick = {
                    isSuggestionDialogOpen = false
                    tripToSuggest = null
                    isLoading = false
                    promptText = ""
                }) {
                    Text("Close")
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isLoading && promptText.isNotBlank(),
                    onClick = {
                        isLoading = true
                        CoroutineScope(Dispatchers.IO).launch {
                            val newText = UpdateSuggestion(aiSuggestion, promptText)
                            suggestionDao.updateSuggestion(suggestionId, newText)
                            withContext(Dispatchers.Main) {
                                aiSuggestion = newText
                                isLoading = false
                                promptText = ""
                            }
                        }
                    }
                ) {
                    Text("Send Prompt to AI")
                }
            },
            title = {
                Text("Destination Suggestion")
            },
            text = {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Processing with AI...")
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = aiSuggestion,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp)) // <- espaço entre texto e prompt

                        TextField(
                            value = promptText,
                            onValueChange = { promptText = it },
                            label = { Text("What would you like to change?") },
                            modifier = Modifier.fillMaxWidth()
                        )
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