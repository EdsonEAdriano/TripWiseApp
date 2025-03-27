package com.example.tripwiseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tripwiseapp.models.LoginViewModel
import com.example.tripwiseapp.models.UserViewModel
import com.example.tripwiseapp.screens.LoginScreen
import com.example.tripwiseapp.screens.MainScreen
import com.example.tripwiseapp.screens.RegisterScreen
import com.example.tripwiseapp.ui.theme.TripWiseAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripWiseAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val navController = rememberNavController()
    val userViewModel = UserViewModel()
    val loginViewModel = LoginViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "TripWiseApp")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = "RegisterScreen"
            ) {
                composable(route = "RegisterScreen") {
                    RegisterScreen(
                        userViewModel = userViewModel,
                        onNavigateTo = {
                            navController.navigate(it)
                        })
                }

                composable(route = "LoginScreen") {
                    LoginScreen(
                        loginViewModel = loginViewModel,
                        onNavigateTo = {
                            navController.navigate(it)
                        })
                }

                composable(route = "MainScreen") {
                    MainScreen(
                        onNavigateTo = {
                            navController.navigate(it)
                        })
                }
            }
        }
    }
}