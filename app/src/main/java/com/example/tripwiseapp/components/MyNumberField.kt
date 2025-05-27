package com.example.tripwiseapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MyNumberField(
    label: String = "",
    value: Double = 0.0,
    onValueChange: (Double) -> Unit
) {
    var text by remember { mutableStateOf( value.dp.value.toString() ) }
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    fun formatCurrency(value: Double): String {
        val formatted = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value / 100.0)
        return formatted.replace("R$ ", "R$")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { input ->
                if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    val parsedValue = input.toDoubleOrNull() ?: 0.0
                    text = input
                    onValueChange(parsedValue) // Retorna o valor numérico
                }
            },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "Ícone de dinheiro"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }


}