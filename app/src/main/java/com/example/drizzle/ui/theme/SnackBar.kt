package com.example.drizzle.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackBar(
    snackbarData: SnackbarData,
    shape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = Color.Black,
    contentColor: Color = Color.White,
) {
    Snackbar(
        snackbarData = snackbarData,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor
    )
}