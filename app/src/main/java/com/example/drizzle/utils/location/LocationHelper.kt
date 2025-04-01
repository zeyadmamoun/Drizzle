package com.example.drizzle.utils.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationHelper(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun getLocation(): Flow<Pair<Double, Double>> = callbackFlow {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)
                if (location.lastLocation != null) {
                    trySend(
                        Pair(
                            location.lastLocation!!.latitude,
                            location.lastLocation!!.latitude
                        )
                    )
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback) // Cleanup
        }

    }
}

//Log.i("HomeViewModel", "onLocationResult: lat = $latitude")
//Log.i("HomeViewModel", "onLocationResult: lat = $longitude")