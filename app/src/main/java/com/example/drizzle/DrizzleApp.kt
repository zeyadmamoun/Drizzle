package com.example.drizzle

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.asFlow
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.drizzle.screens.home.HomeScreen
import com.example.drizzle.screens.settings.SettingsScreen
import com.example.drizzle.ui.theme.CustomSnackBar
import com.example.drizzle.ui.theme.DrizzleHomeTopAppBar
import com.example.drizzle.ui.theme.DrizzleSettingsTopAppBar
import com.example.drizzle.utils.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrizzleApp(connectivityObserver: ConnectivityObserver? = null) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackbarData ->
                CustomSnackBar(snackbarData = snackbarData)
            }
        },
        topBar = {
            when (currentDestination?.destination?.route) {
                "com.example.drizzle.Home" -> DrizzleHomeTopAppBar(
                    scrollBehavior
                ) { navController.navigate(Settings) }
                "com.example.drizzle.Settings" -> DrizzleSettingsTopAppBar { navController.navigateUp() }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        scope.launch {
            connectivityObserver?.let {
                it.isConnected.asFlow().collectLatest { state ->
                    if (!state) {
                        snackBarHostState.showSnackbar(
                            message = "No Internet Connection",
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = Home,
        ) {
            composable<Home> {
                HomeScreen(paddingValues = innerPadding)
            }

            composable<Settings> {
                SettingsScreen(paddingValues = innerPadding)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DrizzleAppPreview() {
    DrizzleApp()
}

// Routes
@Serializable
object Home

@Serializable
object Settings

@Serializable
object Favorites

@Serializable
object WeatherAlert

