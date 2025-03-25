package com.example.drizzle

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drizzle.screens.HomeScreen
import com.example.drizzle.ui.theme.coolWeatherEnd
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrizzleTopAppBar(scrollBehavior: TopAppBarScrollBehavior) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text("Drizzle", style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    tint = Color.White,
                    contentDescription = "Settings"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = coolWeatherEnd,
            titleContentColor = Color.White
        ),
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    tint = Color.White,
                    contentDescription = "Favorites"
                )
            }

            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    tint = Color.White,
                    contentDescription = "Settings"
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrizzleApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = { DrizzleTopAppBar(scrollBehavior) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Home,
        ) {
            composable<Home> {
                HomeScreen(paddingValues = innerPadding)
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

