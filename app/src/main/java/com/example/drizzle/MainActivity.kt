package com.example.drizzle

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.drizzle.ui.theme.DrizzleTheme
import com.example.drizzle.utils.connectivity.ConnectivityObserver

class MainActivity : ComponentActivity() {
    private lateinit var connectivityObserver: ConnectivityObserver
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = ConnectivityObserver(this)
        enableEdgeToEdge()
        setContent {
            DrizzleTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    DrizzleApp()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityObserver.unregisterCallBack()
    }
}