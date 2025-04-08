package com.example.tripwiseapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun MyPasswordField(
    label: String,
    value: String,
    errorMessage: String = "",
    onValueChange: (String) -> Unit
) {

    var isPasswordVisibility = remember {
        mutableStateOf(false)
    }

    var isTouched = remember {
        mutableStateOf(false)
    }

    val focusRequester = FocusRequester()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = value ,
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
                    if (it.isFocused)
                        isTouched.value = true
                },
            trailingIcon = {
                IconButton(onClick = {
                    isPasswordVisibility.value = !isPasswordVisibility.value
                }) {
                    Icon(
                        imageVector =  if (isPasswordVisibility.value)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = "Show/Hide password")
                }
            },
            visualTransformation = if (isPasswordVisibility.value)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            isError = (isTouched.value && value.isBlank()) || isTouched.value && errorMessage != "" && value != errorMessage,
            supportingText = {
                if (isTouched.value && errorMessage.isNotBlank()) {
                    Text(text = errorMessage)
                }
            }
        )
    }
}