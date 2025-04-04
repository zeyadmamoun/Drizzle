package com.example.drizzle.screens.map

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.drizzle.R
import com.example.drizzle.ui.theme.hourSection
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel
import java.io.IOException
import java.util.Locale

@Composable
fun MapScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: MapViewModel = koinViewModel(),
    isFavorite: Boolean = false,
    navigateToFavoriteDetail:(lat: Double,lon: Double) -> Unit,
    navigateBack: () -> Unit
) {

    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(30.0444, 31.2357), 2f)
    }
    var currentCoord by remember { mutableStateOf<LatLng?>(null) }
    var currentMarkerState by remember { mutableStateOf<MarkerState?>(null) }
    var currentCity by remember { mutableStateOf("") }
    var currentContinent by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding()),
        contentAlignment = Alignment.BottomEnd
    ) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            mapColorScheme = ComposeMapColorScheme.LIGHT,
            onMapClick = { position ->
                currentCoord = position
                currentMarkerState = MarkerState(position)
                val result = getAddress(context, position)
                if (result != null) {
                    currentCity = result.first
                    currentContinent = result.second
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            currentMarkerState?.let {
                Marker(
                    state = it,
                    title = currentCity,
                )
            }
        }

        Card(
            colors = CardDefaults.elevatedCardColors(
                containerColor = hourSection
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (currentCoord == null) {
                    Text(
                        stringResource(R.string.location_not_selected),
                        style = MaterialTheme.typography.titleLarge
                    )
                } else {
                    LocationCard(
                        currentCity,
                        currentContinent,
                        currentCoord,
                        viewModel,
                        isFavorite = isFavorite,
                        navigateToFavoriteDetail = navigateToFavoriteDetail,
                        navigateBack
                    )
                }
            }
        }
    }
}

@Composable
fun LocationCard(
    currentCity: String,
    currentContinent: String,
    currentCoordinate: LatLng?,
    viewModel: MapViewModel,
    isFavorite: Boolean = false,
    navigateToFavoriteDetail:(lat: Double,lon: Double) -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        if (currentCity.isEmpty() || currentContinent.isEmpty()) {
            CircularProgressIndicator()
        } else {
            Text(
                currentCity,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                currentContinent,
                style = MaterialTheme.typography.titleSmall
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    if (currentCoordinate != null){
                        if (isFavorite){
                            navigateToFavoriteDetail(currentCoordinate.latitude,currentCoordinate.longitude)
                        } else {
                            viewModel.changeDefaultCoordinates(currentCoordinate.latitude,currentCoordinate.longitude)
                            viewModel.changeDefaultCity(currentCity)
                            navigateBack()
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.choose_location))
            }
        }
    }

}

fun getAddress(context: Context, location: LatLng): Pair<String, String>? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val city: String?
    val country: String

    try {
        val addresses = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        )
        if (addresses.isNullOrEmpty()) {
            return null
        }
        val tempAddress = addresses[0]
        city = tempAddress.locality ?: tempAddress.subAdminArea ?: "Unknown City"
        country = tempAddress.adminArea ?: "Unknown Country"
        return Pair(city, country)
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}