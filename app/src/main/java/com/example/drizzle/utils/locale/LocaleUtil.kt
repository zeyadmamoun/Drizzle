package com.example.drizzle.utils.locale

import android.content.Context
import android.content.res.Configuration
import java.util.*

fun updateAppLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)

    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}