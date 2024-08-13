package com.example.eventtrackingapp

import com.example.eventtrackingapp.ViewModel.UserViewModel
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
import screens.CreateAccountScreen
import screens.EventListScreen
import screens.ForgotPasswordScreen
import screens.LoginScreen

object Routes {
    const val LOGIN = "login"
    const val CREATE_ACCOUNT = "createAccount"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val EVENT_LIST = "eventList"
}

class MainActivity : ComponentActivity() {
    private val requestSmsPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (it.value) {
                // Permission is granted
            } else {
                // Permission is denied
                // Show a message or handle the denied permission
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestSmsPermissions()

        setContent {
            EventTrackingAppTheme {
                val navController = rememberNavController()
                Surface {
                    NavigationComponent(navController)
                }
            }
        }
    }

    private fun requestSmsPermissions() {
        requestSmsPermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS
            )
        )
    }
}

@Composable
fun NavigationComponent(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) { LoginScreen(navController, userViewModel) }
        composable(Routes.CREATE_ACCOUNT) { CreateAccountScreen(navController, userViewModel) }
        composable(Routes.FORGOT_PASSWORD) { ForgotPasswordScreen(navController, userViewModel) }
        composable(Routes.EVENT_LIST) { EventListScreen(navController, userViewModel) }
    }
}
