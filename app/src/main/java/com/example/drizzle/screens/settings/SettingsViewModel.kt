package com.example.drizzle.screens.settings

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drizzle.repository.LanguageSettings
import com.example.drizzle.repository.LocationSettings
import com.example.drizzle.repository.SettingsPreferencesRepository
import com.example.drizzle.repository.TemperatureSettings
import com.example.drizzle.repository.WindSpeedSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsViewModel(
    private val settingsPreferencesRepository: SettingsPreferencesRepository
) : ViewModel(){

    private var _language = MutableStateFlow("")
    val language: StateFlow<String> = _language

    private var _temperature = MutableStateFlow("")
    val temperature: StateFlow<String> = _temperature

    private var _location = MutableStateFlow("")
    val location: StateFlow<String> = _location

    private var _windSpeed = MutableStateFlow("")
    val windSpeed: StateFlow<String> = _windSpeed

    private var _defaultCity = MutableStateFlow("")
    val defaultCity: StateFlow<String> = _defaultCity

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                settingsPreferencesRepository.languageSettings.collectLatest {
                    _language.value = it
                }
            }

            launch {
                settingsPreferencesRepository.temperatureSettings.collectLatest {
                    _temperature.value = it
                }
            }

            launch {
                settingsPreferencesRepository.locationSettings.collectLatest {
                    _location.value = it
                }
            }

            launch {
                settingsPreferencesRepository.windSpeedSettings.collectLatest {
                    _windSpeed.value = it
                }
            }

            launch {
                settingsPreferencesRepository.defaultCity.collectLatest {
                    _defaultCity.value = it
                }
            }
        }


    }

    // all functions to change settings in shared preference
    fun saveLanguagePreference(languageSettings: LanguageSettings){
        viewModelScope.launch(Dispatchers.IO){
            settingsPreferencesRepository.saveLanguagePreference(languageSettings)
        }
    }

    fun saveTemperaturePreference(temperatureSettings: TemperatureSettings){
        viewModelScope.launch(Dispatchers.IO){
            settingsPreferencesRepository.saveTemperaturePreference(temperatureSettings)
        }
    }

    fun saveLocationPreference(locationSettings: LocationSettings){
        viewModelScope.launch(Dispatchers.IO){
            settingsPreferencesRepository.saveLocationPreference(locationSettings)
        }
    }

    fun saveWindSpeedPreference(windSpeedSettings: WindSpeedSettings){
        viewModelScope.launch(Dispatchers.IO){
            settingsPreferencesRepository.saveWindSpeedPreference(windSpeedSettings)
        }
    }


}