package com.example.drizzle.splashscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.drizzle.R
import com.example.drizzle.ui.theme.DrizzleTheme
import com.example.drizzle.utils.location.LocationPermissionDialog
import com.example.drizzle.utils.location.isLocationEnabled
import com.example.drizzle.utils.location.navigateToMainActivity
import com.example.drizzle.utils.location.openAppSettings
import com.example.drizzle.utils.location.openLocationSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrizzleTheme {
                Surface {
                    AnimationBlock()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AnimationBlock() {
    val context = LocalContext.current
    val activity = context as Activity

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    // We ask for the location Permission
    val locationPermission = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermission.allPermissionsGranted) {
        if (!isLocationEnabled(activity)) {
            openLocationSettings(context)
        } else {
            LaunchedEffect(Unit) {
                delay(2000)
                navigateToMainActivity(context)
            }
        }
    } else {
        LocationPermissionDialog(
            onConfirm = {
                if (locationPermission.shouldShowRationale) {
                    openAppSettings(context)
                } else {
                    locationPermission.launchMultiplePermissionRequest()
                }
            },
            onDismiss = {
                activity.finish()
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .height(300.dp)
                .width(400.dp),
            progress = { progress },
        )
        Text(
            "Drizzle",
            style = MaterialTheme.typography.displayLarge
                .copy(fontSize = 48.sp)
        )
    }
}