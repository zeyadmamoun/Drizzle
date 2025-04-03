package com.example.drizzle.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drizzle.repository.SettingsPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(
    private val settingsRepository: SettingsPreferencesRepository,
) : ViewModel() {

    fun changeDefaultCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO){
            settingsRepository.saveCurrentCoordinate(lat, lon)
        }
    }

    fun changeDefaultCity(city: String) {
        viewModelScope.launch(Dispatchers.IO){
            settingsRepository.saveDefaultCity(city)
        }
    }
}