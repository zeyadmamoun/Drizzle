package com.example.drizzle

import android.app.Application
import com.example.drizzle.data.LocalDataSource
import com.example.drizzle.data.LocalDataSourceImpl
import com.example.drizzle.screens.home.HomeViewModel
import com.example.drizzle.repository.WeatherRepository
import com.example.drizzle.repository.WeatherRepositoryImpl
import com.example.drizzle.screens.settings.SettingsViewModel
import com.example.drizzle.utils.connectivity.ConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DrizzleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appModule = module {

            single { ConnectivityObserver(get()) }

            single<LocalDataSource> {
                LocalDataSourceImpl()
            }

            single<WeatherRepository> {
                WeatherRepositoryImpl(get())
            }

            viewModel { HomeViewModel(get(),get()) }
            viewModel { SettingsViewModel() }
        }

        startKoin {
            androidLogger()
            androidContext(this@DrizzleApplication)
            modules(appModule)
        }
    }
}