package com.example.tripwiseapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MyRatioGroupField(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            options.forEach { option ->
                if (option.isNotBlank()){
                    RadioButton(
                        selected = (value == option),
                        onClick = {
                            onValueChange(option)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option, fontSize = 16.sp)
                }
            }
        }
    }
}