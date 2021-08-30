package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme
import com.notnotme.brewdogdiy.ui.theme.Typography

@Composable
fun LoadingMessageBox(
    modifier: Modifier = Modifier,
    text: String,
    space: Dp,
    extraContent: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp)
        )
        Spacer(
            modifier = Modifier.padding(space)
        )
        Text(
            style = Typography.body1,
            text = text,
            textAlign = TextAlign.Center
        )
        extraContent()
    }
}

@Composable
fun ErrorMessageBox(
    modifier: Modifier = Modifier,
    text: String,
    space: Dp,
    extraContent: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = Icons.Default.Error,
            contentDescription = null, // Illustrative
            colorFilter = ColorFilter.tint(MaterialTheme.colors.error, BlendMode.SrcAtop),
            modifier = Modifier.size(64.dp)
        )
        Spacer(
            modifier = Modifier.padding(space)
        )
        Text(
            style = Typography.body1,
            text = text,
            textAlign = TextAlign.Center
        )
        extraContent()
    }
}

// region Previews

@Composable
@Preview(showBackground = true)
fun LoadingMessageBoxPreview() {
    BrewdogTheme {
        LoadingMessageBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "Loading...",
            space = 16.dp
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorMessageBoxPreview() {
    BrewdogTheme {
        ErrorMessageBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "Unknown error",
            space = 16.dp
        )
    }
}

@Composable
@Preview(showBackground = true)
fun LoadingMessageBoxFullScreenPreview() {
    BrewdogTheme {
        LoadingMessageBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            text = "Loading...",
            space = 16.dp
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorMessageBoxFullScreenPreview() {
    BrewdogTheme {
        ErrorMessageBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            text = "Unknown error",
            space = 16.dp
        )
    }
}

// endregion Previews
