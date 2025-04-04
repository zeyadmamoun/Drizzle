package com.example.drizzle.screens.weatherAlert

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.drizzle.model.alert.Alert
import com.example.drizzle.repository.SettingsPreferencesRepository
import com.example.drizzle.repository.WeatherRepository
import com.example.drizzle.screens.favorites.FakeWeatherRepository
import com.example.drizzle.utils.getOrAwaitValueFromFlow
import com.example.drizzle.utils.location.LocationHelper
import io.mockk.mockk
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherAlertViewModelTest{
    private lateinit var settingsRepo: SettingsPreferencesRepository
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var weatherAlertViewModel: WeatherAlertViewModel

    private val alerts = mutableListOf(
        Alert(
            locationName = "Cairo",
            latitude = 29.8,
            longitude = 30.58,
            hour = 10,
            minute = 5,
            isActive = false
        ), Alert(
            locationName = "Fayoum",
            latitude = 29.8,
            longitude = 30.58,
            hour = 12,
            minute = 38,
            isActive = true
        ), Alert(
            locationName = "Ismailia",
            latitude = 12.7,
            longitude = 17.8,
            hour = 5,
            minute = 30,
            isActive = true
        )
    )

    @Before
    fun setup(){
        settingsRepo = mockk(relaxed = true)
        weatherRepository = FakeWeatherRepository(alerts = alerts)
        val locationHelper: LocationHelper = mockk()
        weatherAlertViewModel = WeatherAlertViewModel(
            settingsRepo,
            weatherRepository,
            locationHelper,
            ApplicationProvider.getApplicationContext()
        )
    }

    @Test
    fun addAlert_AddNewAlert_checkForSizeIncrements(){
        val alert = Alert(
            id = 18.toString(),
            locationName = "Paris",
            latitude = 29.8,
            longitude = 30.58,
            hour = 12,
            minute = 38,
            isActive = true
        )

        weatherAlertViewModel.addAlert(alert.hour,alert.minute)
        val result = weatherAlertViewModel.alertList.getOrAwaitValueFromFlow()
        print(result)

        assertNotNull(result)
    }
}