package com.example.tripwiseapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp


@Composable
fun MyTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var isTouched = remember {
        mutableStateOf(false)
    }
    var focusRequester = remember {
        FocusRequester()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                isTouched.value = true
                onValueChange(it)
            },
            singleLine = true,
            label = {
                Text(text = label)
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusEvent {
                    if (it.hasFocus)
                        isTouched.value = true
                },
            isError = isTouched.value && value.isBlank(),
            supportingText = {
                if (isTouched.value && value.isBlank()) {
                    Text(text = "Field ${label} is required")
                }
            }
        )
    }

}