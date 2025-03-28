package com.example.drizzle.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drizzle.R
import com.example.drizzle.ui.theme.DrizzleTheme
import com.example.drizzle.ui.theme.coolGradient
import com.example.drizzle.ui.theme.hourSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = koinViewModel(),
) {
    val currentWeather by viewModel.currentWeather.collectAsState()
    val hourlyForecast by viewModel.hourlyWeather.collectAsState()
    val weeklyForecast by viewModel.weeklyWeather.collectAsState()

    LazyColumn(
        modifier = Modifier
            .padding(bottom = paddingValues.calculateBottomPadding())
            .fillMaxSize(),
    ) {
        item { TemperatureSection(currentWeather) }
        item { Spacer(Modifier.height(8.dp)) }
        item { HourlyTemperatureSection(hourlyForecast) }
        item { Spacer(Modifier.height(8.dp)) }
        item { WeekTemperatureSection(weeklyForecast) }
    }
}

@Composable
fun TemperatureSection(
    state: CurrentWeatherUiState
) {
    Box(
        modifier = Modifier
            .height(500.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = coolGradient, // Gradient colors
                    start = Offset(0f, 0f), // Start from top-left
                    end = Offset(1000f, 1000f) // End at bottom-right
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp)
        ) {
            Text(
                "${state.dayOfWeek}, ${state.day} ${state.month}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.7f)
                ),
            )
            Text(
                "${state.city}, ${state.country}",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
            Image(
                painter = painterResource(state.icon),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )

            Row {
                Text(
                    state.temperature,
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    stringResource(R.string.celsius_mark),
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall
                        .copy(fontSize = 10.sp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Icon(
                    painter = painterResource(R.drawable.humidty),
                    tint = Color.White,
                    contentDescription = "Settings"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    state.humidity,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White
                    ),
                )
                Spacer(modifier = Modifier.width(32.dp))
                Icon(
                    painter = painterResource(R.drawable.wind),
                    tint = Color.White,
                    contentDescription = "Settings"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    state.windSpeed,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White
                    ),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                state.weatherDesc,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.7f)
                ),
            )
        }

    }
}

@Composable
fun HourlyTemperatureSection(hourlyForecast: List<HourForecast>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = hourSection),
        modifier = Modifier
            .height(180.dp)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        ) {
            Text(
                "hourly forecast",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                ),
            )
            Spacer(Modifier.height(16.dp))
            LazyRow {
                items(hourlyForecast) {
                    HourTempColumn(it)
                }
            }
        }
    }
}

@Composable
fun WeekTemperatureSection(weeklyForecast: List<DayWeatherRow>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = hourSection),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        ) {
            Text(
                "Weekly forecast",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                ),
            )
            Spacer(Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                items(weeklyForecast) {
                    DayTempRow(it)
                }
            }
        }
    }
}

@Composable
fun HourTempColumn(hourForecast: HourForecast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(hourForecast.image),
            modifier = Modifier.size(40.dp),
            contentDescription = null
        )
        Text(
            hourForecast.temperature,
            style = MaterialTheme.typography.bodyLarge
                .copy(color = Color.White)
        )
        Text(
            "${hourForecast.hour}:00",
            style = MaterialTheme.typography.bodyLarge
                .copy(color = Color.White)
        )
    }
}

@Composable
fun DayTempRow(dayForecast: DayWeatherRow) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .fillMaxWidth()
    ) {
        Row {
            Column {
                Text(
                    dayForecast.dayOfWeek,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "${dayForecast.dayOfMonth} ${dayForecast.month}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    ),
                )
            }
        }
        Image(
            painter = painterResource(dayForecast.icon),
            modifier = Modifier.size(40.dp),
            contentDescription = null
        )
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                "${dayForecast.temperature} / ",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                " feels like ${dayForecast.feelsLike}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                ),
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeScreenPreview() {
    DrizzleTheme(
        darkTheme = true
    ) { HomeScreen() }
}

//@Preview(showBackground = true)
//@Composable
//private fun HourTempColumnPreview() {
//    HourTempColumn(listOfHourForecast[0])
//}

@Preview(showBackground = true)
@Composable
private fun DayTempRowPreview() {
    DayTempRow(
        DayWeatherRow(
            dayOfWeek = "Fri",
            dayOfMonth = "23th",
            month = "MARCH",
            feelsLike = "18",
            icon = R.drawable.d02,
            temperature = "20"
        )
    )
}