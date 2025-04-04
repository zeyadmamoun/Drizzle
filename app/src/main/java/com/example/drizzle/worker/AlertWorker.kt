package com.example.drizzle.worker

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.drizzle.network.RemoteDataSource
import com.example.drizzle.utils.connectivity.ConnectivityObserver
import com.example.drizzle.utils.notification.NotificationHelper
import kotlinx.coroutines.flow.first

class AlertWorker(appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {

    private val notificationHelper = NotificationHelper(appContext)
    private val connectivityObserver = ConnectivityObserver(appContext)

    private val latitude = inputData.getDouble("latitude",0.0)
    private val longitude = inputData.getDouble("longitude",0.0)
    private val isActive = inputData.getBoolean("isActive",false)

    override suspend fun doWork(): Result {
        if (!isActive){
            return Result.success()
        }

        val isConnected = connectivityObserver.isConnected.asFlow().first()
        if (isConnected){
            val weather = RemoteDataSource.getCurrentWeather(latitude,longitude).toWeatherObject()

            notificationHelper.showNotification(
                temperature = weather.mainTemp,
                weatherCondition = weather.weatherDesc
            )
        } else {
            notificationHelper.showNotification(
                temperature = 0,
                weatherCondition = "Unknown"
            )
        }

        return Result.success()
    }
}