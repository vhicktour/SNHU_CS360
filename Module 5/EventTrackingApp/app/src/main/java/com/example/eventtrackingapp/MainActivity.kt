package com.example.eventtrackingapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eventtrackingapp.ui.theme.EventTrackingAppTheme

class MainActivity : ComponentActivity() {
    private val requestSmsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Proceed with SMS functionality.
        } else {
            // Permission is denied. Show a message to the user.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestSmsPermission()

        setContent {
            EventTrackingAppTheme {
                val navController = rememberNavController()
                Surface {
                    NavigationComponent(navController)
                }
            }
        }
    }

    private fun requestSmsPermission() {
        requestSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
    }
}

@Composable
fun NavigationComponent(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userViewModel) }
        composable("createAccount") { CreateAccountScreen(navController, userViewModel) }
        composable("forgotPassword") { ForgotPasswordScreen(navController, userViewModel) }
        composable("eventList") { EventListScreen(navController, userViewModel) }
    }
}
