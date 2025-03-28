package com.example.drizzle.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.drizzle.R
import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.repository.WeatherRepository
import com.example.drizzle.utils.connectivity.ConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import okio.IOException
import retrofit2.HttpException

class HomeViewModel(
    private val repository: WeatherRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val TAG = "HomeViewModel"

    private var _currentWeather: MutableStateFlow<CurrentWeatherUiState> =
        MutableStateFlow(CurrentWeatherUiState())
    val currentWeather: StateFlow<CurrentWeatherUiState> = _currentWeather

    private var _hourlyWeather: MutableStateFlow<List<HourForecast>> = MutableStateFlow(emptyList())
    val hourlyWeather: StateFlow<List<HourForecast>> = _hourlyWeather

    private var _weeklyWeather: MutableStateFlow<List<DayWeatherRow>> =
        MutableStateFlow(emptyList())
    val weeklyWeather: StateFlow<List<DayWeatherRow>> = _weeklyWeather

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val isConnected = connectivityObserver.isConnected.asFlow().first()
            if (isConnected) {
                try {
                    launch {
                        val result = getCurrentWeatherData(29.3084, 30.8428)
                        Log.i(TAG, result.toString())
                        updateCurrentWeatherState(result)
                    }
                    launch {
                        val forecastList = getHourlyWeatherData(29.3084, 30.8428)
                        setupHourlyWeatherDate(forecastList)
                        setWeeklyWeatherData(forecastList)
                    }
                } catch (ex: IOException) {
                    Log.i(TAG, ex.toString())
                } catch (ex: HttpException) {
                    Log.i(TAG, ex.toString())
                }
            }
        }
    }

    private suspend fun getCurrentWeatherData(lat: Double, lon: Double): WeatherDTO {
        return repository.getCurrentWeather(true, lat, lon)
    }

    private suspend fun getHourlyWeatherData(lat: Double, lon: Double): List<WeatherDTO> {
        return repository.getHourlyWeather(lat, lon)
    }

    /* function to handle the three hours gap between Hourly weather
    * through duplication of every item tree times and pass it to UI
    */
    private fun setupHourlyWeatherDate(hourlyWeather: List<WeatherDTO>) {
        val formattedList = mutableListOf<HourForecast>()
        hourlyWeather.take(8).forEach { weatherDTO ->
            repeat(3) {
                val entry = HourForecast(
                    temperature = weatherDTO.mainTemp.toString(),
                    hour = Instant.fromEpochSeconds(weatherDTO.date.toLong())
                        .plus(it, DateTimeUnit.HOUR, TimeZone.UTC)
                        .toLocalDateTime(TimeZone.UTC)
                        .hour
                        .toString(),
                    image = weatherDTO.icon
                )
                formattedList.add(entry)
            }
        }
        _hourlyWeather.value = formattedList
    }

    /* function to handle updating of Weekly weather section
    * through taking every first forecast of the next 5 days and pass it to UI
    */
    private fun setWeeklyWeatherData(weatherList: List<WeatherDTO>) {
        val formattedList = mutableListOf<DayWeatherRow>()
        for (i in weatherList.indices) {
            if (i % 8 == 0) {
                weatherList[i].apply {
                    val dt = getLocalDateTime(date.toLong())
                    val entry = DayWeatherRow(
                        dayOfWeek = dt.dayOfWeek.toString(),
                        dayOfMonth = dt.dayOfMonth.toString(),
                        month = dt.month.toString(),
                        feelsLike = feelsLike.toString(),
                        icon = icon,
                        temperature = mainTemp.toString()
                    )
                    formattedList.add(entry)
                }
            }
        }
        _weeklyWeather.value = formattedList
    }

    //responsible for updating the main temperature section by calling the api
    // and update _currentWeather StateFlow
    private fun updateCurrentWeatherState(weatherDTO: WeatherDTO) {
        val date = getLocalDateTime(weatherDTO.date.toLong())
        _currentWeather.update {
            it.copy(
                dayOfWeek = date.dayOfWeek.toString(),
                day = date.dayOfMonth.toString(),
                month = date.month.toString(),
                temperature = weatherDTO.mainTemp.toString(),
                humidity = weatherDTO.humidity.toString(),
                windSpeed = weatherDTO.windSpeed.toString(),
                weatherDesc = weatherDTO.weatherDesc,
                city = weatherDTO.name,
                country = weatherDTO.country,
                icon = weatherDTO.icon
            )
        }
    }

    // function to parse the time from unix time to local Date and time
    private fun getLocalDateTime(unixTime: Long) = Instant.fromEpochSeconds(unixTime)
        .toLocalDateTime(TimeZone.UTC)

}

data class CurrentWeatherUiState(
    val dayOfWeek: String = "",
    val day: String = "",
    val month: String = "",
    val temperature: String = "",
    val humidity: String = "",
    val windSpeed: String = "",
    val weatherDesc: String = "",
    val city: String = "",
    val country: String = "",
    val icon: Int = R.drawable.d02
)

data class HourForecast(
    val image: Int = R.drawable.d02,
    val temperature: String,
    val hour: String
)

data class DayWeatherRow(
    val temperature: String,
    val dayOfWeek: String,
    val dayOfMonth: String,
    val month: String,
    val icon: Int,
    val feelsLike: String
)