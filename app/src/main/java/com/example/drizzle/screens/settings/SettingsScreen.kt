package com.example.drizzle.screens.settings

import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drizzle.R
import com.example.drizzle.repository.LanguageSettings
import com.example.drizzle.repository.LocationSettings
import com.example.drizzle.repository.TemperatureSettings
import com.example.drizzle.repository.WindSpeedSettings
import com.example.drizzle.ui.theme.hourSection
import com.example.drizzle.utils.locale.updateAppLocale
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 48.dp),
    viewModel: SettingsViewModel = koinViewModel(),
    navigateToMap: () -> Unit
) {
    val context = LocalContext.current
    val languageState by viewModel.language.collectAsState()
    val temperatureState by viewModel.temperature.collectAsState()
    val locationState by viewModel.location.collectAsState()
    val windSpeedState by viewModel.windSpeed.collectAsState()
    val defaultCityState by viewModel.defaultCity.collectAsState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        LanguageSettingSection(languageState,
            action = { languageSettings ->
                viewModel.saveLanguagePreference(languageSettings)

                val lang: String = when(languageSettings.name){
                    LanguageSettings.ENGLISH.name -> "en"
                    LanguageSettings.ARABIC.name -> "ar"
                    else -> Resources.getSystem().configuration.locales[0].language  // this line get the device language
                }

                if (lang == "en" || lang == "ar"){
                    updateAppLocale(context,lang)
                }
            }
        )
        TemperatureSettingSection(
            temperatureState,
            action = { temperatureSettings ->
                viewModel.saveTemperaturePreference(temperatureSettings)
            }
        )
        LocationSettingSection(
            locationState,
            defaultCityState,
            action = { locationSettings ->
                viewModel.saveLocationPreference(locationSettings)
            },
            navigateToMap
        )
        WindSpeedSettingSection(
            windSpeedState,
            action = { windSpeedSettings ->
                viewModel.saveWindSpeedPreference(windSpeedSettings)
            }
        )
    }
}

@Composable
fun LanguageSettingSection(language: String, action: (LanguageSettings) -> Unit = {}) {
    val radioOptions = listOf(R.string.arabic, R.string.english, R.string.device_default)
    var selectedOption by remember { mutableIntStateOf(radioOptions[0]) }

    selectedOption = when (language) {
        LanguageSettings.ARABIC.name -> radioOptions[0]
        LanguageSettings.ENGLISH.name -> radioOptions[1]
        else -> radioOptions[2]
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = hourSection),
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                .selectableGroup()
        ) {
            Text(
                stringResource(R.string.layout_preferences),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                ),
            )
            Spacer(Modifier.height(16.dp))
            Row {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    selectedOption = text
                                    when (text) {
                                        radioOptions[0] -> action(LanguageSettings.ARABIC)
                                        radioOptions[1] -> action(LanguageSettings.ENGLISH)
                                        else -> action(LanguageSettings.DEFAULT)
                                    }
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null // null recommended for accessibility with screen readers
                        )
                        Text(
                            text = stringResource(text),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TemperatureSettingSection(temperature: String, action: (TemperatureSettings) -> Unit = {}) {
    val switchOptions = listOf(R.string.celsius, R.string.kelvin, R.string.fahrenheit)
    var selectedOption by remember { mutableIntStateOf(switchOptions[0]) }

    selectedOption = when (temperature) {
        TemperatureSettings.CELSIUS.name -> switchOptions[0]
        TemperatureSettings.KELVIN.name -> switchOptions[1]
        else -> switchOptions[2]
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = hourSection),
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                .selectableGroup()
        ) {
            Text(
                stringResource(R.string.temperature),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                ),
            )
            Spacer(Modifier.height(12.dp))
            Column {
                switchOptions.forEach { text ->
                    Row(
                        Modifier
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(text),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Text(
                            text = when (text) {
                                switchOptions[0] -> {
                                    stringResource(R.string.celsius_mark)
                                }

                                switchOptions[1] -> {
                                    stringResource(R.string.kelvin_mark)
                                }

                                else -> {
                                    stringResource(R.string.fahrenheit_mark)
                                }
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .weight(1f)
                        )
                        Switch(
                            checked = (text == selectedOption),
                            onCheckedChange = {
                                selectedOption = text
                                when (text) {
                                    switchOptions[0] -> {
                                        action(TemperatureSettings.CELSIUS)
                                    }

                                    switchOptions[1] -> {
                                        action(TemperatureSettings.KELVIN)
                                    }

                                    else -> {
                                        action(TemperatureSettings.FAHRENHEIT)
                                    }
                                }
                            },
                            modifier = Modifier.padding(end = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationSettingSection(
    location: String,
    defaultCityState: String,
    action: (LocationSettings) -> Unit = {},
    navigateToMap: () -> Unit
) {
    val switchOptions = listOf(R.string.gps, R.string.maps)
    var selectedOption by remember { mutableIntStateOf(switchOptions[0]) }

    selectedOption = when (location) {
        LocationSettings.GPS.name -> switchOptions[0]
        else -> switchOptions[1]
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = hourSection),
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .selectableGroup()
        ) {
            Text(
                stringResource(R.string.location),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                ),
            )
            Spacer(Modifier.height(12.dp))
            Column {
                switchOptions.forEach { text ->
                    Row(
                        Modifier
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(text),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        )
                        Switch(
                            checked = (text == selectedOption),
                            onCheckedChange = {
                                selectedOption = text
                                when (selectedOption) {
                                    switchOptions[0] -> action(LocationSettings.GPS)
                                    else -> {
                                        action(LocationSettings.MAPS)
                                        navigateToMap()
                                    }
                                }
                            },
                            modifier = Modifier.padding(end = 24.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.current_Location) + ": " + defaultCityState,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun WindSpeedSettingSection(windSpeed: String, action: (WindSpeedSettings) -> Unit = {}) {
    val radioOptions = listOf(R.string.meter, R.string.mile)
    var selectedOption by remember { mutableIntStateOf(radioOptions[0]) }

    selectedOption = when (windSpeed) {
        WindSpeedSettings.Meter.name -> radioOptions[0]
        else -> radioOptions[1]
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = hourSection),
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                .selectableGroup()
        ) {
            Text(
                stringResource(R.string.wind_speed),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                ),
            )
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    selectedOption = text
                                    when (selectedOption) {
                                        radioOptions[0] -> action(WindSpeedSettings.Meter)
                                        else -> action(WindSpeedSettings.Mile)
                                    }
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null
                        )
                        Text(
                            text = stringResource(text),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun LanguageSectionPreview() {
    SettingsScreen(navigateToMap = {})
}