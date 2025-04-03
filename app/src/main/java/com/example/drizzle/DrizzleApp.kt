package com.example.drizzle

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.asFlow
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.drizzle.screens.favoriteDetail.FavoriteDetailsScreen
import com.example.drizzle.screens.favorites.FavoriteScreen
import com.example.drizzle.screens.home.HomeScreen
import com.example.drizzle.screens.map.MapScreen
import com.example.drizzle.screens.settings.SettingsScreen
import com.example.drizzle.screens.weatherAlert.WeatherAlertScreen
import com.example.drizzle.ui.theme.CustomSnackBar
import com.example.drizzle.ui.theme.DrizzleFavoriteDetailsTopAppBar
import com.example.drizzle.ui.theme.DrizzleFavoritesTopAppBar
import com.example.drizzle.ui.theme.DrizzleHomeTopAppBar
import com.example.drizzle.ui.theme.DrizzleMapTopAppBar
import com.example.drizzle.ui.theme.DrizzleSettingsTopAppBar
import com.example.drizzle.ui.theme.DrizzleWeatherAlertTopAppBar
import com.example.drizzle.utils.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrizzleApp(connectivityObserver: ConnectivityObserver) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
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
                    scrollBehavior,
                    onFavoriteIconClicked = { navController.navigate(Favorites) },
                    onSettingsIconClicked = { navController.navigate(Settings) },
                    onAlertIconClicked = { navController.navigate(WeatherAlert) }
                )

                "com.example.drizzle.Settings" -> DrizzleSettingsTopAppBar { navController.navigateUp() }
                "com.example.drizzle.Map" -> {
                    val title =
                        if (navController.previousBackStackEntry?.destination?.route == "com.example.drizzle.Settings") {
                            R.string.default_location
                        } else {
                            R.string.favorites
                        }
                    DrizzleMapTopAppBar(title) { navController.navigateUp() }
                }

                "com.example.drizzle.Favorites" -> DrizzleFavoritesTopAppBar { navController.navigateUp() }
                "com.example.drizzle.WeatherAlert" -> DrizzleWeatherAlertTopAppBar { navController.navigateUp() }
                else -> DrizzleFavoriteDetailsTopAppBar(scrollBehavior) {
                    navController.popBackStack(
                        Favorites,
                        false
                    )
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->

        LaunchedEffect(Unit) {
            connectivityObserver.isConnected.asFlow().distinctUntilChanged()
                .collectLatest { state ->
                    if (!state) {
                        snackBarHostState.showSnackbar(
                            message = "No Internet Connection",
                            duration = SnackbarDuration.Indefinite
                        )
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
                SettingsScreen(
                    paddingValues = innerPadding,
                    navigateToMap = { navController.navigate(Map) })
            }

            composable<Map> {
                MapScreen(
                    paddingValues = innerPadding,
                    isFavorite = navController.previousBackStackEntry?.destination?.route == "com.example.drizzle.Favorites",
                    navigateToFavoriteDetail = { lat, lon ->
                        navController.navigate(
                            FavoriteDetails(
                                lat = lat,
                                lon = lon,
                                cityId = null
                            )
                        )
                    },
                    navigateBack = { navController.navigateUp() }
                )
            }

            composable<Favorites> {
                FavoriteScreen(
                    paddingValues = innerPadding,
                    navigateToMap = { navController.navigate(Map) },
                    navigateToFavoriteDetails = { lat, lon, cityId ->
                        navController.navigate(FavoriteDetails(lat, lon, cityId))
                    }
                )
            }

            composable<FavoriteDetails> { backStackEntry ->
                val favoriteDetails: FavoriteDetails = backStackEntry.toRoute()
                FavoriteDetailsScreen(
                    favoriteDetails.lat,
                    favoriteDetails.lon,
                    favoriteDetails.cityId
                )
            }

            composable<WeatherAlert> {
                WeatherAlertScreen(
                    paddingValues = innerPadding
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DrizzleAppPreview() {
    val connectivityObserver = koinInject<ConnectivityObserver>()
    DrizzleApp(connectivityObserver)
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

@Serializable
object Map

@Serializable
data class FavoriteDetails(val lat: Double, val lon: Double, val cityId: Int?)
