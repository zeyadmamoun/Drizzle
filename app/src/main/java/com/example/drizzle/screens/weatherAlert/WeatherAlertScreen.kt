package com.example.drizzle.screens.weatherAlert

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drizzle.R
import com.example.drizzle.screens.favorites.CityCard
import com.example.drizzle.ui.theme.cardGradients
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAlertScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: WeatherAlertViewModel = koinViewModel()
) {

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding(), start = 8.dp, end = 8.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (showBottomSheet){
            ModalBottomSheet(
                onDismissRequest = {showBottomSheet = false},
                sheetState = sheetState
            ) { }
        }
        if (false) {
            AnimatedVisibility(
                visibleState = visibleState,
                enter = fadeIn(animationSpec = spring(dampingRatio = DampingRatioLowBouncy)),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn {
                    items(4) {
                        SwipeToDismissItem(
                            itemNumber = it,
                            swipeAction = {  },
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInVertically(
                                        animationSpec = spring(
                                            stiffness = StiffnessLow,
                                            dampingRatio = DampingRatioLowBouncy
                                        ),
                                        initialOffsetY = { it * (it + 1) }
                                    )
                                )
                        )
                    }
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(R.drawable.no_alarm),
                    contentDescription = "Empty List"
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(R.string.no_alart_yet),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        ExtendedFloatingActionButton(
            onClick = { showBottomSheet = true },
            icon = { Icon(Icons.Outlined.Notifications, "Extended floating action button.") },
            text = { Text(text = stringResource(R.string.add_alert)) },
            modifier = Modifier.padding(
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 8.dp
            )
        )
    }
}

@Composable
fun AlertCard(
    itemNumber: Int,
    favoriteCity: CityCard,
    navigateToFavoriteDetails: (lat: Double, lon: Double, cityId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(140.dp)
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = cardGradients[itemNumber % 4],
                    start = Offset(0f, 0f),       // Start from top
                    end = Offset(0f, Float.POSITIVE_INFINITY) // End at bottom
                )
            )
            .clickable {
                Log.i("FavoriteDetailsViewMode", "FavoriteCard: id: ${favoriteCity.cityId}")
                navigateToFavoriteDetails(
                    favoriteCity.lat,
                    favoriteCity.lon,
                    favoriteCity.cityId
                )
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.Absolute.Left,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    favoriteCity.name,
                    style = MaterialTheme.typography.displayMedium
                )

                Text(
                    favoriteCity.date,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    favoriteCity.weatherDesc,
                    style = MaterialTheme.typography.titleSmall.copy(
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        favoriteCity.temperature,
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        stringResource(favoriteCity.temperatureUnit),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(favoriteCity.icon),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SwipeToDismissItem(
    itemNumber: Int,
    modifier: Modifier = Modifier,
    swipeAction: (Int) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.EndToStart) {
                coroutineScope.launch {
                    delay(1.seconds)

                }
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        backgroundContent = {
            val background by animateColorAsState(
                targetValue = when (swipeToDismissBoxState.currentValue) {
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    else -> Color.Transparent
                },
                label = "Animate Background Color"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Icon(
                        Icons.Outlined.Delete,
                        "Extended floating action button.",
                        tint = Color.White
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        stringResource(R.string.remove_city),
                        style = MaterialTheme.typography.titleSmall
                            .copy(color = Color.White)
                    )
                }
            }
        },
        modifier = modifier
    ) {
        // TODO: Add card component
    }
}

@Composable
fun BottomSheetContent(modifier: Modifier = Modifier) {
    Column {

    }
}

