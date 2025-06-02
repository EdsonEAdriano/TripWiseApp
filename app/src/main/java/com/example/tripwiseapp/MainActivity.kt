package com.example.tripwiseapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tripwiseapp.models.LoginViewModel
import com.example.tripwiseapp.models.UserViewModel
import com.example.tripwiseapp.screens.LoginScreen
import com.example.tripwiseapp.screens.MainScreen
import com.example.tripwiseapp.screens.RegisterScreen
import com.example.tripwiseapp.ui.theme.TripWiseAppTheme

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.contentColorFor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.tripwiseapp.database.AppDatabase
import com.example.tripwiseapp.models.LoginViewModelFactory
import com.example.tripwiseapp.models.TripViewModel
import com.example.tripwiseapp.models.TripViewModelFactory
import com.example.tripwiseapp.models.UserViewModelFactory
import com.example.tripwiseapp.screens.AboutScreen
import com.example.tripwiseapp.screens.TripScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val navController = rememberNavController()

    val ctx = LocalContext.current
    val userDao = AppDatabase.getDatabase(ctx).userDao()
    val userViewModel : UserViewModel = viewModel(
        factory = UserViewModelFactory(userDao)
    )

    val loginViewModel : LoginViewModel = viewModel(
        factory = LoginViewModelFactory(userDao)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = "TripWiseApp"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        bottomBar = {
            val backStack = navController.currentBackStackEntryAsState()
            val currentDestination = backStack.value?.destination

            val hideBottomBarScreens = listOf("RegisterScreen", "LoginScreen")
            val shouldShowBottomBar = currentDestination?.route !in hideBottomBarScreens

            if (shouldShowBottomBar) {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colorScheme.primary
                ) {
                    BottomNavigationItem(
                        selected =
                        currentDestination?.hierarchy?.any {
                            it.route == "MainScreen"
                        } == false,
                        onClick = { navController.navigate("MainScreen") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Main Screen",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                    BottomNavigationItem(
                        selected =
                        currentDestination?.hierarchy?.any {
                            it.route == "TripScreen"
                        } == true,
                        onClick = { navController.navigate("TripScreen") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Trip",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                    BottomNavigationItem(
                        selected =
                        currentDestination?.hierarchy?.any {
                            it.route == "AboutScreen"
                        } == true,
                        onClick = { navController.navigate("AboutScreen") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "About",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                }
            }
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
                            navController.navigate(it) {
                                if (it != "LoginScreen") {
                                    popUpTo(0)
                                }
                            }
                        })
                }

                composable(route = "LoginScreen") {
                    LoginScreen(
                        loginViewModel = loginViewModel,
                        onNavigateTo = {
                            navController.navigate(it) {
                                popUpTo(0)
                            }
                        })
                }

                composable(route = "MainScreen") {
                    MainScreen(
                        onNavigateTo = {
                            navController.navigate(it)
                        }
                    )
                }

                composable(route = "AboutScreen") {
                    AboutScreen()
                }


                composable(route = "TripScreen") {
                    TripScreen(
                        null,
                        onNavigateTo = {
                            navController.navigate(it) {
                                popUpTo(0)
                            }
                        }
                    )
                }

                composable(route = "TripScreen/{id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.IntType}
                    )
                ) {
                        backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id")
                    TripScreen(
                        id,
                        onNavigateTo = {
                            navController.navigate(it) {
                                popUpTo(0)
                            }
                        }
                    )
                }

            }
        }
    }
}