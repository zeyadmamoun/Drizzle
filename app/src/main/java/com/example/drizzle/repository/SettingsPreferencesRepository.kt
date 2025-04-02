package com.example.drizzle.repository

import android.health.connect.datatypes.units.Temperature
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    val languageSettings: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading language.", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[LANGUAGE_SETTING] ?: LanguageSettings.ENGLISH.name
        }

    val temperatureSettings: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading temperature.", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[TEMPERATURE_UNIT] ?: TemperatureSettings.CELSIUS.name
        }

    val locationSettings: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e("HomeViewModel", "Error reading location setting.", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[LOCATION_SETTING] ?: LocationSettings.GPS.name
        }

    val windSpeedSettings: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading wind speed.", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[WIND_SPEED_SETTING] ?: WindSpeedSettings.Meter.name
        }

    val currentCoordinates: Flow<Pair<Double,Double>> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading coordinates.", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            Pair(preferences[CURRENT_LATITUDE]?: 30.0444,preferences[CURRENT_LONGITUDE]?:31.2357)
        }

    val defaultCity: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading default city.", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[DEFAULT_CITY] ?: ""
        }


    //---------------------------------------------------------------------------------------------------
    suspend fun saveLanguagePreference(setting: LanguageSettings) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_SETTING] = setting.name
        }
    }

    suspend fun saveTemperaturePreference(temperature: TemperatureSettings) {
        dataStore.edit { preferences ->
            preferences[TEMPERATURE_UNIT] = temperature.name
        }
    }

    suspend fun saveLocationPreference(location: LocationSettings) {
        dataStore.edit { preferences ->
            preferences[LOCATION_SETTING] = location.name
        }
    }

    suspend fun saveWindSpeedPreference(windSpeedSettings: WindSpeedSettings) {
        dataStore.edit { preferences ->
            preferences[WIND_SPEED_SETTING] = windSpeedSettings.name
        }
    }

    suspend fun saveCurrentCoordinate(lat: Double, lon: Double) {
        dataStore.edit { preferences ->
            preferences[CURRENT_LATITUDE] = lat
            preferences[CURRENT_LONGITUDE] = lon
        }
    }

    suspend fun saveDefaultCity(city: String){
        dataStore.edit { preferences ->
            preferences[DEFAULT_CITY] = city
        }
    }

    companion object {
        const val TAG = "SettingsPreferencesRepo"
        val LANGUAGE_SETTING = stringPreferencesKey("language_setting")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperature_setting")
        val LOCATION_SETTING = stringPreferencesKey("location_setting")
        val WIND_SPEED_SETTING = stringPreferencesKey("windSpeed_setting")
        val CURRENT_LATITUDE = doublePreferencesKey("current_latitude")
        val CURRENT_LONGITUDE = doublePreferencesKey("current_longitude")
        val DEFAULT_CITY = stringPreferencesKey("default_city")
    }
}

enum class LanguageSettings {
    ARABIC, ENGLISH, DEFAULT
}

enum class TemperatureSettings {
    CELSIUS, KELVIN, FAHRENHEIT
}

enum class LocationSettings {
    MAPS, GPS
}

enum class WindSpeedSettings {
    Meter, Mile
}