package com.notnotme.brewdogdiy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    /*customize*/
)

private val LightColorPalette = lightColors(
    /*customize*/
)

@Composable
fun BrewdogDIYTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        },
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}