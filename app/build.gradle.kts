plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.20"
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.drizzle"
    compileSdk = 35

    androidResources {
        generateLocaleConfig = true
    }

    defaultConfig {
        applicationId = "com.example.drizzle"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Navigation Component
    implementation(libs.androidx.navigation.compose)

    //Kotlinx DateTime
    implementation(libs.kotlinx.datetime)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.okhttp)

    // Lottie Animation for splash screen
    implementation(libs.lottie.compose)

    // Compose Accompanist permission
    implementation (libs.accompanist.permissions)

    // ViewModel with Compose
    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.compose)

    // Koin
    implementation (libs.koin.androidx.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.constraintlayout.compose.android)
    implementation(libs.androidx.junit.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    // Android Maps Compose composables for the Maps SDK for Android
    implementation(libs.maps.compose)

    // Work Manager
    implementation(libs.androidx.work.runtime.ktx)

    // hamcrest
    testImplementation (libs.hamcrest)
    testImplementation (libs.hamcrest.library)
    androidTestImplementation (libs.hamcrest)
    androidTestImplementation (libs.hamcrest.library)

    //MockK
    testImplementation (libs.mockk.android)
    testImplementation (libs.mockk.agent)

    //Robolectric
    testImplementation (libs.robolectric)

    //kotlinx-coroutines
    testImplementation (libs.org.jetbrains.kotlinx.kotlinx.coroutines.test)
    androidTestImplementation (libs.org.jetbrains.kotlinx.kotlinx.coroutines.test2)
    
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}