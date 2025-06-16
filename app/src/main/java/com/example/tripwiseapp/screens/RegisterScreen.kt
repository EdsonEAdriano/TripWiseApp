package com.example.tripwiseapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripwiseapp.components.ErrorDialog
import com.example.tripwiseapp.components.MyPasswordField
import com.example.tripwiseapp.components.MyTextField
import com.example.tripwiseapp.models.UserViewModel
import com.example.tripwiseapp.ui.theme.TripWiseAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(userViewModel: UserViewModel, onNavigateTo:(String) -> Unit, onUserIdChange: (Long) -> Unit) {
    var registerUser = userViewModel.uiState.collectAsState()
    val ctx = LocalContext.current

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "Register",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )


            MyTextField(
                label = "User",
                value = registerUser.value.user,
                onValueChange = {
                    userViewModel.onUserChange(it)
                },
            )
            MyTextField(
                label = "E-mail",
                value = registerUser.value.email,
                onValueChange = {
                    userViewModel.onEmailChange(it)
                }
            )
            MyPasswordField(
                label = "Password",
                value = registerUser.value.password,
                errorMessage = registerUser.value.validatePassord(),
                onValueChange = {
                    userViewModel.onPasswordChange(it)
                })
            MyPasswordField(
                label = "Confirm password",
                value = registerUser.value.confirmPassword,
                errorMessage = registerUser.value.validateConfirmPassword() ,
                onValueChange = {
                    userViewModel.onConfirmPassword(it)
                })

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    userViewModel.register({ id ->
                        Log.d("ID GERADO", id.toString())
                        onUserIdChange(id)
                    })
                }
            ) {
                Text(text = "Register user")
            }

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    onNavigateTo("LoginScreen")
                }
            ) {
                Text(text = "Already have an account? Log in here!")
            }





            if (registerUser.value.errorMessage.isNotBlank()) {
                ErrorDialog(
                    error = registerUser.value.errorMessage,
                    onDismissRequest =  {
                        userViewModel.cleanDisplayValues()
                    }
                )
            }
        }

        LaunchedEffect(registerUser.value.isSaved) {
            if(registerUser.value.isSaved){
                Toast.makeText(ctx, "User registered",
                    Toast.LENGTH_SHORT).show()

                onNavigateTo("MainScreen")
            }
        }
    }
}


@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewRegisterScreen() {
    val userViewModel : UserViewModel = viewModel()

    TripWiseAppTheme {
        RegisterScreen(userViewModel, {}, {})
    }
}