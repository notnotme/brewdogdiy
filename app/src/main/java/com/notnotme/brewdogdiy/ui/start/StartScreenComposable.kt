package com.notnotme.brewdogdiy.ui.start

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.ui.theme.BrewdogDIYTheme
import com.notnotme.brewdogdiy.ui.theme.Typography

@Composable
fun StartScreen(
    navigateToList: () -> Unit,
    navigateToRandom: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                style = Typography.h5,
                text = stringResource(R.string.choice),
                textAlign = TextAlign.Center)

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))

            Button(
                onClick = { navigateToList() }) {
                Text(
                    style = Typography.button,
                    text = stringResource(R.string.browse_all_beers),
                    textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier
                .padding(0.dp, 48.dp)
                .fillMaxWidth())

            Text(
                style = Typography.h5,
                text = stringResource(R.string.no_choice),
                textAlign = TextAlign.Center)

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))

            Button(
                onClick = { navigateToRandom() }) {
                Text(
                    text = stringResource(R.string.peek_a_random_beer),
                    textAlign = TextAlign.Center)
            }
        }
    }
}

// region Previews

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    BrewdogDIYTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            StartScreen({}, {})
        }
    }
}

// endregion Previews
