package com.notnotme.brewdogdiy.ui.common

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme

@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier
) {
    androidx.compose.material.CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colors.secondary
    )
}

// region Previews

@Composable
@Preview(showBackground = true)
fun CircularProgressIndicatorPreview() {
    BrewdogTheme {
        CircularProgressIndicator()
    }
}

// endregion Previews
