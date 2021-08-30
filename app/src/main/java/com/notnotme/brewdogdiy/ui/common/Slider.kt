package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme

@Composable
fun Slider(
    modifier: Modifier = Modifier,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChange: (value: Float) -> Unit = {},
    onValueChangeFinished: () -> Unit = {},
) {
    androidx.compose.material.Slider(
        modifier = modifier,
        value = value,
        valueRange = valueRange,
        steps = steps,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colors.secondary,
            activeTrackColor = MaterialTheme.colors.secondaryVariant
        )
    )
}

// region Previews

@Composable
@Preview(showBackground = true)
fun SliderPreview() {
    BrewdogTheme {
        Box{
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = 50.0f,
                valueRange = 0.0f..100.0f
            )

        }
    }
}

// endregion Previews
