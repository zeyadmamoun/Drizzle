package com.example.drizzle.screens.settings

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import java.util.Locale

class SettingsViewModel : ViewModel(){

    fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}