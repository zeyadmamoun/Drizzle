package com.example.drizzle.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drizzle.R
import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.repository.SettingsPreferencesRepository
import com.example.drizzle.repository.TemperatureSettings
import com.example.drizzle.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FavoritesViewModel(
    private val settingsRepository: SettingsPreferencesRepository,
    private val repository: WeatherRepository
) : ViewModel() {

    // settings preferences attributes flows
    private lateinit var temperatureUnit: String

    private var _cityCardList: MutableStateFlow<List<CityCard>?> = MutableStateFlow(null)
    val cityCardList: StateFlow<List<CityCard>?> = _cityCardList

    init {
        viewModelScope.launch {
            settingsRepository.temperatureSettings.collect {
                temperatureUnit = it
            }
        }
    }

    fun refreshData(){
        viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            repository.getAllCities().collectLatest { list ->
                updateCityList(list)
            }
        }
    }

    fun removeFavoriteCity(cityId: Int) = viewModelScope.launch{
        repository.removeCityEntries(cityId)
    }

    private fun updateCityList(cities: List<WeatherDTO>) {
        val cityCardsList = cities.groupBy { it.cityId }.map { item -> item.value.first() }
        _cityCardList.value = cityCardsList.map { item ->
            CityCard(
                name = item.name,
                icon = item.icon,
                temperature = getTheTempCalculation(item.mainTemp),
                weatherDesc = item.weatherDesc,
                date =  getLocalDateTime(item.date.toLong()).date.toString(),
                temperatureUnit = getTheTempUnit(),
                cityId = item.cityId,
                lat = item.weatherLat,
                lon = item.weatherLon
            )
        }.toList()
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

    private fun getLocalDateTime(unixTime: Long) = Instant.fromEpochSeconds(unixTime)
        .toLocalDateTime(TimeZone.UTC)
}

data class CityCard(
    val lat: Double,
    val lon: Double,
    val cityId: Int,
    val name: String,
    val icon: Int,
    val temperature: String,
    val temperatureUnit: Int,
    val date: String,
    val weatherDesc: String
)