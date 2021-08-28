package com.notnotme.brewdogdiy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Color(0xFF212121),
    primaryVariant = Color(0xFF484848),
    secondary = Color(0xFF29B6F6),
    secondaryVariant = Color(0xFF73E8FF),
    error = Color(0xFFB00020)
)

private val DarkColorPalette = darkColors(
    primary = Color(0xFF212121),
    primaryVariant = Color(0xFF484848),
    surface = Color(0xFF484848),
    secondary = Color(0xFF29B6F6),
    secondaryVariant = Color(0xFF73E8FF),
    error = Color(0xFFCF6679)
)

@Composable
fun BrewdogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
