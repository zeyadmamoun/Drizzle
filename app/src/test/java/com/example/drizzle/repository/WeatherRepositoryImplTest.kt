package com.example.drizzle.repository

import com.example.drizzle.data.LocalDataSource
import com.example.drizzle.model.alert.Alert
import com.example.drizzle.model.current.WeatherDTO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private lateinit var weatherRepositoryImplTest: WeatherRepositoryImpl
    private lateinit var fakeLocalDataSource: LocalDataSource

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
    fun setup() {
        fakeLocalDataSource = FakeLocalDataSource(weatherDTOList = localCurrentWeather, alerts = alerts)
        weatherRepositoryImplTest = WeatherRepositoryImpl(fakeLocalDataSource)
    }


    @Test
    fun collectListOfLocalCurrentWeather_returnTwoItems() = runTest {
        val result = weatherRepositoryImplTest.getLocalCurrentWeather().first()

        assertThat(result.size, `is`(2))
        assertThat(result[0].cityId, `is`(localCurrentWeather[0].cityId))
        assertThat(result[1].cityId, `is`(localCurrentWeather[1].cityId))
    }

    @Test
    fun collectAlerts_returnTwoItems() = runTest {
        val result = weatherRepositoryImplTest.getAlerts().first()

        assertThat(result.size, `is`(3))
        assertThat(result[0].id, `is`(alerts[0].id))
        assertThat(result[1].id, `is`(alerts[1].id))
    }

    @Test
    fun insertLocalCurrentEntry_addWeatherForHour_returnTheSame() = runTest {
        val newHourForecast = WeatherDTO(
            entryId = 13,
            icon = 2,
            weatherLat = 29.8,
            weatherLon = 30.58,
            weatherDesc = "cloud overcast",
            mainTemp = 38,
            cityId = 15654,
            name = "Mecca",
            date = 216511123,
            timezone = 7200,
            humidity = 80,
            country = "SA",
            feelsLike = 40,
            windSpeed = 3.825
        )

        weatherRepositoryImplTest.insertLocalCurrentEntry(newHourForecast)
        val result = weatherRepositoryImplTest.getLocalCurrentWeather().first()[2]

        assertThat(result.cityId, `is`(newHourForecast.cityId))
        assertThat(result.name, `is`(newHourForecast.name))
        assertThat(result.country, `is`(newHourForecast.country))
    }

    @Test
    fun deleteAllCurrentLocal_returnListWithZeroItems() = runTest {
        //When
        weatherRepositoryImplTest.deleteLocalCurrentWeather()
        val result = weatherRepositoryImplTest.getLocalCurrentWeather().first()

        //Then
        assertThat(result.size, `is`(0))
    }

    @Test
    fun addAlert_enterNewAlertWithId50_returnTheSame() = runTest{
        val newAlert = Alert(
            id = 50.toString(),
            locationName = "Cairo",
            latitude = 29.8,
            longitude = 30.58,
            hour = 10,
            minute = 5,
            isActive = false
        )

        weatherRepositoryImplTest.addAlert(alert = newAlert)
        val result = weatherRepositoryImplTest.getAlerts().first()[3]

        assertThat(result.id, `is`(newAlert.id))
    }
}