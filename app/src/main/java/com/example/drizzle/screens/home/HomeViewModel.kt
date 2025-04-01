package com.example.drizzle.screens.home

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.drizzle.R
import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.repository.LocationSettings
import com.example.drizzle.repository.SettingsPreferencesRepository
import com.example.drizzle.repository.TemperatureSettings
import com.example.drizzle.repository.WeatherRepository
import com.example.drizzle.ui.theme.coldGradient
import com.example.drizzle.ui.theme.coolGradient
import com.example.drizzle.ui.theme.hotGradient
import com.example.drizzle.utils.connectivity.ConnectivityObserver
import com.example.drizzle.utils.location.LocationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val repository: WeatherRepository,
    private val settingsRepository: SettingsPreferencesRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val locationHelper: LocationHelper
) : ViewModel() {

    // settings preferences attributes flows
    private lateinit var temperatureUnit: String
    private lateinit var windSpeedUnit: String
    private lateinit var locationSetting: String

    // stateFlows that update the UI
    private var _currentWeather: MutableStateFlow<CurrentWeatherUiState?> =
        MutableStateFlow(null)
    val currentWeather: StateFlow<CurrentWeatherUiState?> = _currentWeather

    private var _hourlyWeather: MutableStateFlow<List<HourForecast>> = MutableStateFlow(emptyList())
    val hourlyWeather: StateFlow<List<HourForecast>> = _hourlyWeather

    private var _weeklyWeather: MutableStateFlow<List<DayWeatherRow>> =
        MutableStateFlow(emptyList())
    val weeklyWeather: StateFlow<List<DayWeatherRow>> = _weeklyWeather

    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        collectLocalDate()
    }

    // in the init we only make the collection from database
    fun collectLocalDate() {
        viewModelScope.launch {
            repository.getLocalCurrentWeather().collectLatest { list ->
                Log.i(TAG, "im inside the collection : ")
                val currentTime = Clock.System.now().epochSeconds.toInt()

                // checking if any cached time equals current time
                if (list.isNotEmpty()) {
                    for (i in 0 until list.size - 1) {
                        if (currentTime >= list[i].date && currentTime <= list[i + 1].date) {
                            updateCurrentWeatherState(list[i])
                        } else {
                            updateCurrentWeatherState(list[0])
                        }
                    }

                    // passing the hourly forecast list to the ui
                    setupHourlyWeatherDate(list.filter { it != list[0] })
                    setWeeklyWeatherData(list)

                    _error.value = ""
                }
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            // collect the preferences
            temperatureUnit = settingsRepository.temperatureSettings.first()
            windSpeedUnit = settingsRepository.windSpeedSettings.first()
            locationSetting = settingsRepository.locationSettings.first()

            // refresh cached data if there is internet connection
            val isConnected = connectivityObserver.isConnected.asFlow().first()
            if (isConnected) {
                _isLoading.value = true
                // 1 - here we check on the user selected source of location and get data upon it as Pair()
                val coordinates = async {
                    when (locationSetting) {
                        LocationSettings.MAPS.name -> {
                            settingsRepository.currentCoordinates.first()
                        }
                        else -> {
                            locationHelper.getLocation().first()
                        }
                    }
                }

                try {
                    // 2- second we get the data from server
                    val current = async {
                        getCurrentWeatherData(
                            coordinates.await().first,
                            coordinates.await().second
                        )
                    }
                    val forecastList = async {
                        getHourlyWeatherData(
                            coordinates.await().first,
                            coordinates.await().second
                        )
                    }

                    // 3- delete all cached data
                    repository.deleteLocalCurrentWeather()

                    // 4- caching the new data and that will cause the collection flow above to update the UI
                    runBlocking {
                        repository.insertLocalCurrentEntry(current.await())
                        forecastList.await().forEach {
                            repository.insertLocalCurrentEntry(it)
                        }
                    }
                    _error.value = ""   // here to make sure that there is noe error
                } catch (ex: IOException) {
                    Log.i(TAG, ex.toString())
                    _error.value = "check your connection"
                } catch (ex: HttpException) {
                    Log.i(TAG, ex.toString())
                    _error.value = "refresh the screen again"
                } catch (ex: SocketTimeoutException){
                    Log.i(TAG, ex.toString())
                    refreshData()
                    _error.value = "refresh the screen again"
                }
            }
            _isLoading.value = false
        }
    }

    // obtaining the data of current weather from server
    private suspend fun getCurrentWeatherData(lat: Double, lon: Double): WeatherDTO {
        return repository.getCurrentWeather(true, lat, lon)
    }

    // obtaining the list of 40 item forecast
    private suspend fun getHourlyWeatherData(lat: Double, lon: Double): List<WeatherDTO> {
        return repository.getHourlyForecast(lat, lon)
    }

    /* function to handle the three hours gap between Hourly weather
    * through duplication of every item tree times and pass it to UI
    */
    private fun setupHourlyWeatherDate(hourlyWeather: List<WeatherDTO>) {
        val formattedList = mutableListOf<HourForecast>()
        hourlyWeather.take(8).forEach { weatherDTO ->
            repeat(3) {
                val entry = HourForecast(
                    temperature = getTheTempCalculation(weatherDTO.mainTemp),
                    hour = Instant.fromEpochSeconds(weatherDTO.date.toLong())
                        .plus(it, DateTimeUnit.HOUR, TimeZone.UTC)
                        .toLocalDateTime(TimeZone.UTC)
                        .hour
                        .toString(),
                    image = weatherDTO.icon,
                    tempUnit = getTheTempUnit()
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
                        feelsLike = getTheTempCalculation(feelsLike),
                        icon = icon,
                        temperature = getTheTempCalculation(mainTemp),
                        tempUnit = getTheTempUnit()
                    )
                    formattedList.add(entry)
                }
            }
        }
        _weeklyWeather.value = formattedList
    }


    // function to update the temperature unit on the UI
    private fun getTheTempUnit(): Int {
        return when (temperatureUnit) {
            TemperatureSettings.CELSIUS.name -> R.string.celsius_mark
            TemperatureSettings.KELVIN.name -> R.string.kelvin_mark
            else -> R.string.fahrenheit_mark
        }
    }

    // function to calculate the temperature based on user selected unit like kelvin or celsius
    private fun getTheTempCalculation(temperature: Int): String {
        return when (temperatureUnit) {
            TemperatureSettings.CELSIUS.name -> temperature.toString()
            TemperatureSettings.KELVIN.name -> (temperature + 273).toString()
            else -> ((temperature * (9 / 5)) + 32).toString()
        }
    }

    // function to update the gradient color based on the temperature
    private fun changeBackgroundColor(temperature: Int): List<Color> {
        val celsiusTemp = when (temperatureUnit) {
            TemperatureSettings.KELVIN.name -> temperature - 273
            TemperatureSettings.FAHRENHEIT.name -> ((temperature - 32) * 5) / 9
            else -> temperature
        }

        return when {
            celsiusTemp in 11..30 -> coolGradient
            celsiusTemp < 11 -> coldGradient
            else -> hotGradient
        }
    }

    //responsible for updating the main temperature section by calling the api
    // and update _currentWeather StateFlow
    private fun updateCurrentWeatherState(weatherDTO: WeatherDTO) {
        val date = getLocalDateTime(weatherDTO.date.toLong())
        _currentWeather.update {
            CurrentWeatherUiState(
                dayOfWeek = date.dayOfWeek.toString(),
                day = date.dayOfMonth.toString(),
                month = date.month.toString(),
                temperature = getTheTempCalculation(temperature = weatherDTO.mainTemp),
                humidity = weatherDTO.humidity.toString(),
                windSpeed = weatherDTO.windSpeed.toString(),
                weatherDesc = weatherDTO.weatherDesc,
                city = weatherDTO.name,
                country = weatherDTO.country,
                icon = weatherDTO.icon,
                tempUnit = getTheTempUnit(),
                background = changeBackgroundColor(
                    getTheTempCalculation(temperature = weatherDTO.mainTemp).toInt()
                )
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
    val temperature: String = "20",
    val humidity: String = "",
    val windSpeed: String = "",
    val weatherDesc: String = "",
    val city: String = "",
    val country: String = "",
    val icon: Int = R.drawable.d02,
    val tempUnit: Int = R.string.celsius_mark,
    val background: List<Color>
)

data class HourForecast(
    val image: Int = R.drawable.d02,
    val temperature: String,
    val hour: String,
    val tempUnit: Int
)

data class DayWeatherRow(
    val temperature: String,
    val dayOfWeek: String,
    val dayOfMonth: String,
    val month: String,
    val icon: Int,
    val feelsLike: String,
    val tempUnit: Int
)
