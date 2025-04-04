package com.example.drizzle.screens.weatherAlert

import android.icu.util.Calendar
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drizzle.R
import com.example.drizzle.model.alert.Alert
import com.example.drizzle.ui.theme.cardGradients
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAlertScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: WeatherAlertViewModel = koinViewModel()
) {
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
    val selectedHour = remember { mutableIntStateOf(currentTime.hour) }
    val selectedMinute = remember { mutableIntStateOf(currentTime.minute) }
    val timeText =
        remember { mutableStateOf("${selectedHour.intValue} : ${selectedMinute.intValue}") }
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    val alertList by viewModel.alertList.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding(), start = 8.dp, end = 8.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (alertList.isNotEmpty()) {
            AnimatedVisibility(
                visibleState = visibleState,
                enter = fadeIn(animationSpec = spring(dampingRatio = DampingRatioLowBouncy)),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn {
                    itemsIndexed(alertList) { index, item ->
                        SwipeToDismissItem(
                            itemNumber = index,
                            swipeAction = { alert ->
                                viewModel.removeAlert(alert)
                            },
                            alert = item,
                            updateAlert = { alert ->
                                viewModel.removeAlert(alert)
                                viewModel.addAlert(alert.hour,alert.minute,alert.isActive)
                            },
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
            onClick = { showBottomSheet.value = true },
            icon = { Icon(Icons.Outlined.Notifications, "Extended floating action button.") },
            text = { Text(text = stringResource(R.string.add_alert)) },
            modifier = Modifier.padding(
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 8.dp
            )
        )

        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet.value = false },
                sheetState = sheetState,
            ) {
                BottomSheetContent(
                    sheetState,
                    showBottomSheet,
                    showTimePicker,
                    timeText,
                    selectedHour,
                    selectedMinute,
                    onSaveClicked = { hour, minute ->
                        viewModel.addAlert(hour, minute)
                    }
                )
            }
        }

        if (showTimePicker.value) {
            TimePickerDialog(
                selectedHour = selectedHour,
                selectedMinute = selectedMinute,
                timeText = timeText,
                onDismiss = { showTimePicker.value = false },
                onConfirm = {
                    showTimePicker.value = false
                }
            )
        }
    }
}

@Composable
fun AlertCard(
    itemNumber: Int,
    item: Alert,
    updateAlert: (Alert) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(100.dp)
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
    ) {
        Row(
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    item.locationName,
                    style = MaterialTheme.typography.displayMedium
                        .copy(fontSize = 20.sp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "${item.hour}:${item.minute}",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
            }

            IconButton(
                onClick = {
                    item.isActive = !item.isActive
                    updateAlert(item)
                },
                modifier = Modifier.size(40.dp)
            ) {
                if (item.isActive)
                    Icon(Icons.Filled.Notifications, "Active Alert")
                else
                    Icon(painterResource(R.drawable.notification_off), "Active Alert")
            }
        }
    }
}

@Composable
fun SwipeToDismissItem(
    itemNumber: Int,
    alert: Alert,
    modifier: Modifier = Modifier,
    swipeAction: (Alert) -> Unit = {},
    updateAlert: (Alert) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.EndToStart) {
                coroutineScope.launch {
                    delay(1000)
                    swipeAction(alert)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        Icons.Outlined.Delete,
                        "Extended floating action button.",
                        tint = Color.White
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        stringResource(R.string.remove_alert),
                        style = MaterialTheme.typography.titleSmall
                            .copy(color = Color.White)
                    )
                }
            }
        },
        modifier = modifier
    ) {
        AlertCard(itemNumber, alert, { alert -> updateAlert(alert) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    sheetState: SheetState,
    showBottomSheet: MutableState<Boolean>,
    showTimePicker: MutableState<Boolean>,
    timeText: MutableState<String>,
    hour: MutableIntState,
    minute: MutableIntState,
    onSaveClicked: (Int, Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            stringResource(R.string.daily_time),
            style = MaterialTheme.typography.titleSmall
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
                .clickable {
                    showTimePicker.value = true
                }
        ) {
            Icon(
                painterResource(R.drawable.alarm_ic),
                tint = Color.White,
                contentDescription = null
            )
            Spacer(Modifier.width(32.dp))
            Text(
                timeText.value,
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    onSaveClicked(hour.intValue, minute.intValue)
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.save),
                style = MaterialTheme.typography.titleSmall
            )
        }
        OutlinedButton(
            onClick = {
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.cancel),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    selectedHour: MutableIntState,
    selectedMinute: MutableIntState,
    timeText: MutableState<String>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    selectedMinute.intValue = timePickerState.minute
                    selectedHour.intValue = timePickerState.hour
                    timeText.value = "${selectedHour.intValue} : ${selectedMinute.intValue}"
                    onConfirm()
                },
                modifier = Modifier.width(240.dp)
            ) {
                Text(stringResource(R.string.confirm_selection))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.width(240.dp)
            ) {
                Text(stringResource(R.string.dismiss_picker))
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = timePickerState)
            }
        }
    )
}