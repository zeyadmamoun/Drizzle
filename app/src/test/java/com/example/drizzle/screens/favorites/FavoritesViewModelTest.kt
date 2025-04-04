package com.example.drizzle.screens.favorites

import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.repository.SettingsPreferencesRepository
import com.example.drizzle.repository.WeatherRepository
import com.example.drizzle.utils.getOrAwaitValueFromFlow
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class FavoritesViewModelTest{
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var weatherRepo: WeatherRepository
    private lateinit var settingsRepo: SettingsPreferencesRepository

    private val localCurrentWeather = mutableListOf(
        WeatherDTO(
            entryId = 7,
            icon = 2,
            weatherLat = 29.8,
            weatherLon = 30.58,
            weatherDesc = "rainy",
            mainTemp = 12,
            cityId = 656598,
            name = "Barcelona",
            date = 216511123,
            timezone = 7200,
            humidity = 80,
            country = "SP",
            feelsLike = 40,
            windSpeed = 3.825
        ), WeatherDTO(
            entryId = 5,
            icon = 2,
            weatherLat = 29.8,
            weatherLon = 30.58,
            weatherDesc = "cloud overcast",
            mainTemp = 38,
            cityId = 15654,
            name = "Cairo",
            date = 216511123,
            timezone = 7200,
            humidity = 80,
            country = "EG",
            feelsLike = 40,
            windSpeed = 3.825
        )
    )

    @Before
    fun setup(){
        settingsRepo = mockk(relaxed = true)
        weatherRepo = FakeWeatherRepository(localCurrentWeather)
        viewModel = FavoritesViewModel(settingsRepo,weatherRepo)
    }

    @Test
    fun refreshData_cityCardListIsNotNull() = runTest {
        viewModel.refreshData()

        val result = viewModel.cityCardList.getOrAwaitValueFromFlow()

        assertNotNull(result)
    }

    @Test
    fun removeFavoriteCity_removeFirstFavoriteCity() = runTest {
        // When removing city from favorite
        viewModel.removeFavoriteCity(656598)
        val result = weatherRepo.getAllCities().first()

        //Then
        assertThat(result.size, `is`(1))
    }
}