package com.notnotme.brewdogdiy.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.ui.theme.Typography

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    buttonListClicked: () -> Unit,
    buttonRandomClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = Typography.h5,
                text = "all",
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Button(
                onClick = { buttonListClicked() }) {
                Text(
                    style = Typography.button,
                    text = "ALl",
                    textAlign = TextAlign.Center
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(0.dp, 48.dp)
                    .fillMaxWidth()
            )
            Text(
                style = Typography.h5,
                text = "random",
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Button(
                onClick = { buttonRandomClicked() }) {
                Text(
                    text = "random",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
