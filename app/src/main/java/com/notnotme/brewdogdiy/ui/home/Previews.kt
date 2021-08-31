package com.notnotme.brewdogdiy.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme

@Composable
@Preview(showBackground = true)
fun HomeScreen() {
    BrewdogTheme {
        HomeScreen(
            navigateToCatalog = {},
            navigateToRandom = {},
            navigateToAbv = {},
            navigateToIbu = {},
            navigateToFby = {},
            onUpdateClick = {},
        )
    }
}
