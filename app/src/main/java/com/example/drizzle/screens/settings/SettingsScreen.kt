package com.example.drizzle.screens.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drizzle.R
import com.example.drizzle.ui.theme.hourSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 48.dp),
    viewModel: SettingsViewModel = koinViewModel()
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        LanguageSettingSection()
        TemperatureSettingSection()
        LocationSettingSection()
        WindSpeedSettingSection()
    }
}

@Composable
fun LanguageSettingSection() {
    val radioOptions = listOf(R.string.arabic, R.string.english, R.string.device_default)
    val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(radioOptions[0]) }
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
                                onClick = { onOptionSelected(text) },
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
fun TemperatureSettingSection() {
    val switchOptions = listOf(R.string.celsius, R.string.kelvin, R.string.fahrenheit)
    val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(switchOptions[0]) }
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
                                    stringResource(R.string.celsius_mark)
                                }
                                else -> {
                                    stringResource(R.string.fahrenheit_mark)
                                }
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 2.dp)
                                .weight(1f)
                        )
                        Switch(
                            checked = (text == selectedOption),
                            onCheckedChange = { onOptionSelected(text) },
                            modifier = Modifier.padding(end = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationSettingSection() {
    val switchOptions = listOf(R.string.gps, R.string.maps)
    val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(switchOptions[0]) }
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
                            modifier = Modifier.padding(start = 16.dp)
                                .weight(1f)
                        )
                        Switch(
                            checked = (text == selectedOption),
                            onCheckedChange = { onOptionSelected(text) },
                            modifier = Modifier.padding(end = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WindSpeedSettingSection() {
    val radioOptions = listOf(R.string.meter, R.string.mile)
    val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(radioOptions[0]) }
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
                                onClick = { onOptionSelected(text) },
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

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun LanguageSectionPreview() {
    SettingsScreen()
}