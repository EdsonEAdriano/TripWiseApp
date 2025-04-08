package com.example.tripwiseapp.components

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext


@Composable
fun MyDatePicker(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()


    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                onValueChange(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = {}, // Campo somente leitura
            label = { Text(text = label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    datePickerDialog.show()
                }, // Aqui o clique funciona no campo inteiro
            trailingIcon = {
                IconButton(onClick = {
                    datePickerDialog.show()
                }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecionar Data"
                    )
                }
            }
        )
    }
}

