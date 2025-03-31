package com.example.drizzle.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.drizzle.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrizzleHomeTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsIconClicked: () -> Unit
) {
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

            IconButton(onClick = { onSettingsIconClicked() }) {
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
fun DrizzleSettingsTopAppBar(navigateUp: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(R.string.Settings), style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "back button to home"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = coolWeatherEnd,
            titleContentColor = Color.White
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrizzleMapTopAppBar(navigateUp: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(R.string.default_location), style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "back button to home"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = hourSection,
            scrolledContainerColor = coolWeatherEnd,
            titleContentColor = Color.White
        ),
    )
}