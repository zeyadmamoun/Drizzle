package com.example.drizzle.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.drizzle.model.current.FavoriteWeatherDTO
import com.example.drizzle.model.current.WeatherDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert
    suspend fun insertHourForecast(hourForecast: WeatherDTO)

    @Query("DELETE FROM current")
    suspend fun deleteAllCurrent()

    @Query("SELECT * FROM current order by date ASC")
    fun getCurrentForecast(): Flow<List<WeatherDTO>>

    @Query("DELETE FROM favorites WHERE cityId = :cityId")
    suspend fun removeCityEntries(cityId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCityEntry(entry: FavoriteWeatherDTO)

    @Query("SELECT * FROM favorites WHERE cityId = :cityId ORDER BY date ASC")
    fun getCityForecast(cityId: Int): Flow<List<FavoriteWeatherDTO>>

    @Query("Select * FROM favorites ORDER BY date ASC")
    fun getAllCities(): Flow<List<FavoriteWeatherDTO>>

}