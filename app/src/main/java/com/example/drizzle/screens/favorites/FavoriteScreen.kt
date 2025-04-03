package com.example.drizzle.screens.favorites

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.drizzle.ui.theme.cardGradients
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun FavoriteScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navigateToMap: () -> Unit,
    navigateToFavoriteDetails: (lat: Double, lon: Double, cityId: Int) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val cityList by viewModel.cityCardList.collectAsState()
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding(), start = 8.dp, end = 8.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (!cityList.isNullOrEmpty()) {
            AnimatedVisibility(
                visibleState = visibleState,
                enter = fadeIn(animationSpec = spring(dampingRatio = DampingRatioLowBouncy)),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn {
                    itemsIndexed(cityList!!) { index, city ->
                        SwipeToDismissItem(
                            itemNumber = index,
                            item = city,
                            navigateToFavoriteDetails = navigateToFavoriteDetails,
                            swipeAction = { id -> viewModel.removeFavoriteCity(id) },
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInVertically(
                                        animationSpec = spring(
                                            stiffness = StiffnessLow,
                                            dampingRatio = DampingRatioLowBouncy
                                        ),
                                        initialOffsetY = { it * (index + 1) }
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
                    painter = painterResource(R.drawable.list),
                    contentDescription = "Empty List"
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(R.string.no_fav_yet),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        ExtendedFloatingActionButton(
            onClick = { navigateToMap() },
            icon = { Icon(Icons.Outlined.Add, "Extended floating action button.") },
            text = { Text(text = stringResource(R.string.add_favorite)) },
            modifier = Modifier.padding(
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 8.dp
            )
        )
    }
}

@Composable
fun FavoriteCard(
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
    item: CityCard,
    swipeAction: (Int) -> Unit,
    navigateToFavoriteDetails: (lat: Double, lon: Double, cityId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.EndToStart) {
                coroutineScope.launch {
                    delay(1.seconds)
                    swipeAction(item.cityId)
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
        FavoriteCard(
            itemNumber = itemNumber,
            favoriteCity = item,
            navigateToFavoriteDetails = navigateToFavoriteDetails
        )
    }
}