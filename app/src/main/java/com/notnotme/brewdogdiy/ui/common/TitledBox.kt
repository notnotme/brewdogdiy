package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme
import com.notnotme.brewdogdiy.ui.theme.Typography

@Composable
fun TitledBox(
    modifier: Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            style = Typography.body1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(0.dp, 4.dp),
            text = title.uppercase()
        )
        Divider(
            color = contentColorFor(LocalContentColor.current),
            thickness = 4.dp,
        )
        Surface(
            color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1f) // todo: from theme
        ) {
            content()
        }
    }
}

@Composable
fun ParagraphBox(
    modifier: Modifier,
    title: String,
    text: String,
) {
    TitledBox(
        modifier = modifier,
        title = title
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                modifier = Modifier.padding(16.dp),
                style = Typography.body1,
                text = text,
                textAlign = TextAlign.Justify
            )
        }
    }
}

// region Previews

@Composable
@Preview(showBackground = true)
fun TitledBoxParagraphPreview() {
    BrewdogTheme {
        ParagraphBox(
            modifier = Modifier.padding(16.dp),
            title = "Lorem ipsus tagada",
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        )
    }
}

// endregion Previews
