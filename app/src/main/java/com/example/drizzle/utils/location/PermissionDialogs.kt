package com.example.drizzle.utils.location

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LocationPermissionDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = {  },
        title = { Text("Allow Location Access") },
        text = {
            Text(
                "We need your location to provide accurate weather updates for your area. " +
                        "Without location access, we won't be able to show local weather information."
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Deny")
            }
        }
    )
}