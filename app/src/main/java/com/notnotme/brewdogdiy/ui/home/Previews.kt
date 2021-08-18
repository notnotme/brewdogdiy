package com.notnotme.brewdogdiy.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview(showBackground = true)
fun HomeScreen() {
    HomeScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        buttonListClicked = {},
        buttonRandomClicked = {}
    )
}
