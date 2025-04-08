package com.example.tripwiseapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.tripwiseapp.components.MyPasswordField
import com.example.tripwiseapp.components.MyTextField
import com.example.tripwiseapp.models.LoginViewModel
import com.example.tripwiseapp.models.UserViewModel
import com.example.tripwiseapp.ui.theme.TripWiseAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(loginViewModel: LoginViewModel, onNavigateTo:(String) -> Unit) {
    var loginUser = loginViewModel.uiState.collectAsState()
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
                text = "Login",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            MyTextField(
                label = "E-mail",
                value = loginUser.value.email,
                onValueChange = {
                    loginViewModel.onEmailChange(it)
                }
            )
            MyPasswordField(
                label = "Password",
                value = loginUser.value.password,
                errorMessage = loginUser.value.validatePassord(),
                onValueChange = {
                    loginViewModel.onPasswordChange(it)
                })

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    loginViewModel.login()
                }
            ) {
                Text(text = "Login")
            }



            if (loginUser.value.errorMessage.isNotBlank()) {
                ErrorDialog(
                    error = loginUser.value.errorMessage,
                    onDismissRequest =  {
                        loginViewModel.cleanErrorMessage()
                    }
                )
            }
        }

        LaunchedEffect(loginUser.value.isValid) {
            if(loginUser.value.isValid){
                Toast.makeText(ctx, "User logged",
                    Toast.LENGTH_SHORT).show()

                onNavigateTo("MainScreen")
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewLoginScreen() {
    val loginViewModel : LoginViewModel = viewModel()

    TripWiseAppTheme {
        LoginScreen(loginViewModel, {})
    }
}