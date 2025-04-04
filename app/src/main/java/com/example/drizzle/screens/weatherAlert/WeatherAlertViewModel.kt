package com.example.drizzle.screens.weatherAlert

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.drizzle.model.alert.Alert
import com.example.drizzle.repository.LocationSettings
import com.example.drizzle.repository.SettingsPreferencesRepository
import com.example.drizzle.repository.WeatherRepository
import com.example.drizzle.screens.map.getAddress
import com.example.drizzle.utils.location.LocationHelper
import com.example.drizzle.worker.AlertWorker
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class WeatherAlertViewModel(
    private val settingsRepository: SettingsPreferencesRepository,
    private val repository: WeatherRepository,
    private val locationHelper: LocationHelper,
    private val context: Application
) : ViewModel() {

    private var _alertList : MutableStateFlow<List<Alert>> = MutableStateFlow(emptyList())
    val alertList : StateFlow<List<Alert>> = _alertList

    init {
        viewModelScope.launch {
            repository.getAlerts().collectLatest {
                _alertList.value = it
            }
        }
    }

    fun addAlert(hour: Int, minute: Int,isActive: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            val locationSetting = settingsRepository.locationSettings.first()
            val location = when (locationSetting) {
                LocationSettings.GPS.name -> {
                    locationHelper.getLocation().first()
                }

                else -> settingsRepository.currentCoordinates.first()
            }

            val cityName = getAddress(context, LatLng(location.first, location.second))?.first
            val newAlert = Alert(
                locationName = cityName ?: "Unknown Place",
                latitude = location.first,
                longitude = location.second,
                hour = hour,
                minute = minute,
                isActive = isActive
            )

            startWork(newAlert)
            repository.addAlert(newAlert)
        }
    }

    fun removeAlert(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO){
            WorkManager.getInstance(context).cancelUniqueWork(alert.id)
            repository.deleteAlert(alert)
        }
    }

//    fun updateAlert(alert: Alert){
//        viewModelScope.launch(Dispatchers.IO){
//            repository.deleteAlert(alert)
//            addAlert(alert.hour,alert.minute,alert.isActive)
//            repository.addAlert(alert)
//        }
//    }

    private fun startWork(alert: Alert) {
        val timeInMillis = calculateInitialDelay(alert.hour, alert.minute)
        val data = workDataOf(
            "latitude" to alert.latitude,
            "longitude" to alert.longitude,
            "isActive" to alert.isActive
        )

        val alertWorkRequest = PeriodicWorkRequestBuilder<AlertWorker>(24, TimeUnit.HOURS)
            .setInputData(data)
            .setInitialDelay(timeInMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            alert.id,
            ExistingPeriodicWorkPolicy.UPDATE,
            alertWorkRequest
        )
    }

    private fun calculateInitialDelay(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val scheduledTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // If the time has already passed today, schedule it for tomorrow
        if (now.after(scheduledTime)) {
            scheduledTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        return scheduledTime.timeInMillis - now.timeInMillis
    }


}