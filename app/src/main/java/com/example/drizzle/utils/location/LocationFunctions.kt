package com.example.drizzle.utils.location

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import com.example.drizzle.MainActivity

fun navigateToMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}

fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

fun isLocationEnabled(activity: Activity): Boolean {
    val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}