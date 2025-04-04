package com.example.drizzle

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.drizzle.data.LocalDataSource
import com.example.drizzle.data.LocalDataSourceImpl
import com.example.drizzle.data.WeatherDao
import com.example.drizzle.data.WeatherDatabase
import com.example.drizzle.repository.SettingsPreferencesRepository
import com.example.drizzle.repository.WeatherRepository
import com.example.drizzle.repository.WeatherRepositoryImpl
import com.example.drizzle.screens.favoriteDetail.FavoriteDetailsViewModel
import com.example.drizzle.screens.favorites.FavoritesViewModel
import com.example.drizzle.screens.home.HomeViewModel
import com.example.drizzle.screens.map.MapViewModel
import com.example.drizzle.screens.settings.SettingsViewModel
import com.example.drizzle.screens.weatherAlert.WeatherAlertViewModel
import com.example.drizzle.utils.connectivity.ConnectivityObserver
import com.example.drizzle.utils.location.LocationHelper
import com.example.drizzle.utils.notification.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

private const val WEATHER_SETTINGS = "settings_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = WEATHER_SETTINGS
)

class DrizzleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appModule = module {

            // helpers
            single { ConnectivityObserver(get()) }
            single { LocationHelper(get()) }
            single { NotificationHelper(get()) }
            single<WeatherDao> { WeatherDatabase.getDatabase(get()).weatherDao() }

            single<LocalDataSource> {
                LocalDataSourceImpl(get())
            }

            single<SettingsPreferencesRepository> {
                SettingsPreferencesRepository(dataStore = dataStore)
            }

            single<WeatherRepository> {
                WeatherRepositoryImpl(get())
            }

            viewModel { HomeViewModel(get(), get(), get(),get()) }
            viewModel { SettingsViewModel(get()) }
            viewModel { MapViewModel(get()) }
            viewModel { FavoritesViewModel(get(),get()) }
            viewModel { FavoriteDetailsViewModel(get(),get(),get()) }
            viewModel { WeatherAlertViewModel(get(),get(),get(),get()) }
        }

        startKoin {
            androidLogger()
            androidContext(this@DrizzleApplication)
            modules(appModule)
        }
    }
}