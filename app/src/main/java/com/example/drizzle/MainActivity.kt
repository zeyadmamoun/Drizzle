package com.example.drizzle

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.drizzle.repository.LanguageSettings
import com.example.drizzle.repository.SettingsPreferencesRepository
import com.example.drizzle.ui.theme.DrizzleTheme
import com.example.drizzle.utils.connectivity.ConnectivityObserver
import com.example.drizzle.utils.locale.updateAppLocale
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val connectivityObserver: ConnectivityObserver by inject()
    private val settingsPreferencesRepository: SettingsPreferencesRepository by inject()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // here i collect the language stored in preferences and i change the app upon it
            LaunchedEffect(Unit) {
                settingsPreferencesRepository.languageSettings.collect{ languageSettings ->
                    val lang: String = when(languageSettings){
                        LanguageSettings.ENGLISH.name -> "en"
                        LanguageSettings.ARABIC.name -> "ar"
                        else -> Locale.getDefault().language
                    }

                    if (lang == "en" || lang == "ar"){
                        updateAppLocale(this@MainActivity,lang)
                    }
                }
            }

            DrizzleTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    DrizzleApp(connectivityObserver)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityObserver.unregisterCallBack()
    }
}