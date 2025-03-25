package com.example.drizzle.screens

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drizzle.R
import com.example.drizzle.model.HourForecast
import com.example.drizzle.ui.theme.DrizzleTheme
import com.example.drizzle.ui.theme.coolGradient
import com.example.drizzle.ui.theme.hourSection

@Composable
fun HomeScreen(paddingValues: PaddingValues = PaddingValues()) {
    LazyColumn(
        modifier = Modifier
            .padding(bottom = paddingValues.calculateBottomPadding())
            .fillMaxSize(),
    ) {
        item { TemperatureSection() }
        item { Spacer(Modifier.height(8.dp)) }
        item { HourlyTemperatureSection() }
        item { Spacer(Modifier.height(8.dp)) }
        item { WeekTemperatureSection() }
    }
}

@Composable
fun TemperatureSection(modifier: Modifier = Modifier) {
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
                "Mon, 0.5 June",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.7f)
                ),
            )
            Text(
                "Cairo, Egypt",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
            Image(
                painter = painterResource(R.drawable.sun_cloudy),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )

            Text(
                "18",
                color = Color.White,
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Icon(
                    painter = painterResource(R.drawable.humidty),
                    tint = Color.White,
                    contentDescription = "Settings"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "89",
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
                    "89",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White
                    ),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Moderate rain",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.7f)
                ),
            )
        }

    }
}

@Composable
fun HourlyTemperatureSection(modifier: Modifier = Modifier) {
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
                items(listOfHourForecast) {
                    HourTempColumn(it)
                }
            }
        }
    }
}

@Composable
fun WeekTemperatureSection(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = hourSection),
        modifier = Modifier
            .padding(8.dp)
            .height(480.dp)
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
            LazyColumn {
                items(listOfHourForecast.size){
                    DayTempRow()
                }
            }
        }
    }
}

@Composable
fun HourTempColumn(hourForecast: HourForecast, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(hourForecast.image),
            contentDescription = null
        )
        Text(
            hourForecast.temperature,
            style = MaterialTheme.typography.bodyLarge
                .copy(color = Color.White)
        )
        Text(
            hourForecast.hour,
            style = MaterialTheme.typography.bodyLarge
                .copy(color = Color.White)
        )
    }
}

@Composable
fun DayTempRow(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row {
            Text(
                "Today",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                " (21 mar)",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.5f)
                ),
            )
        }
        Image(
            painter = painterResource(R.drawable.sun_cloudy),
            contentDescription = null
        )
        Row {
            Text(
                "19",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                " feels like 12",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.5f)
                ),
            )
        }
    }
}

val listOfHourForecast = listOf(
    HourForecast(
        image = R.drawable.sun_cloudy,
        "26",
        "12:00"
    ),

    HourForecast(
        image = R.drawable.sun_cloudy,
        "26",
        "12:00"
    ),

    HourForecast(
        image = R.drawable.sun_cloudy,
        "26",
        "12:00"
    ),

    HourForecast(
        image = R.drawable.sun_cloudy,
        "26",
        "12:00"
    ),

    HourForecast(
        image = R.drawable.sun_cloudy,
        "26",
        "12:00"
    ),
    HourForecast(
        image = R.drawable.sun_cloudy,
        "26",
        "12:00"
    ),

    HourForecast(
        image = R.drawable.sun_cloudy,
        "26",
        "12:00"
    ),
)

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

@Preview(showBackground = true)
@Composable
private fun HourTempColumnPreview() {
    HourTempColumn(listOfHourForecast[0])
}

@Preview(showBackground = true)
@Composable
private fun DayTempRowPreview() {
    DayTempRow()
}