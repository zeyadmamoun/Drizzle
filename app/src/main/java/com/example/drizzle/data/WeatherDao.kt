package com.example.drizzle.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.drizzle.model.current.FavoriteWeatherDTO
import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.screens.home.HourForecast
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert
    suspend fun insertHourForecast(hourForecast: WeatherDTO)

    @Query("DELETE FROM current")
    suspend fun deleteAllCurrent()

    @Query("SELECT * FROM current")
    fun getCurrentForecast(): Flow<List<WeatherDTO>>
}