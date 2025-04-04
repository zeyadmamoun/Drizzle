package com.example.drizzle.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.drizzle.model.alert.Alert
import com.example.drizzle.model.current.FavoriteWeatherDTO
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    private lateinit var weatherDao: WeatherDao
    private lateinit var db: WeatherDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        weatherDao = db.weatherDao()
    }

    @After
    fun terminate() = db.close()

    @Test
    fun saveAlert_addingActiveAlert_retrieveSameAlert() = runTest {
        // Given
        val alert = Alert(
            locationName = "Cairo",
            latitude = 29.8,
            longitude = 30.58,
            hour = 12,
            minute = 38,
            isActive = true
        )
        //When
        weatherDao.addAlert(alert)
        val result = weatherDao.getAlerts().first()[0]

        //Then
        assertNotNull(result)
        assertThat(result.id, `is`(alert.id))
        assertThat(result.locationName, `is`(alert.locationName))
        assertThat(result.latitude, `is`(alert.latitude))
        assertThat(result.longitude, `is`(alert.longitude))
        assertThat(result.isActive, `is`(alert.isActive))
    }

    @Test
    fun deleteAlert_addingAndDeleteAlert_getEmptyList() = runTest {
        // Given
        val alert = Alert(
            locationName = "Cairo",
            latitude = 29.8,
            longitude = 30.58,
            hour = 12,
            minute = 38,
            isActive = true
        )
        //When
        weatherDao.addAlert(alert)
        weatherDao.deleteAlert(alert)
        val result = weatherDao.getAlerts().first()

        //Then
        assertThat(result.size, `is`(0))
    }

    @Test
    fun saveCity_addingFavoriteCityEntry_retrieveSameCityById() = runTest {
        // Given
        val cityEntry = FavoriteWeatherDTO(
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
        //When
        weatherDao.addCityEntry(cityEntry)
        val result = weatherDao.getCityForecast(cityEntry.cityId).first()[0]

        //Then
        assertNotNull(result)
        assertThat(result.entryId, `is`(cityEntry.entryId))
        assertThat(result.weatherDesc, `is`(cityEntry.weatherDesc))
        assertThat(result.weatherLat, `is`(cityEntry.weatherLat))
        assertThat(result.weatherLon, `is`(cityEntry.weatherLon))
        assertThat(result.cityId, `is`(cityEntry.cityId))
    }


}