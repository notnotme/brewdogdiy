package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme
import com.notnotme.brewdogdiy.ui.theme.Typography

@Composable
fun Button(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
    onClick: () -> Unit = {}
) {
    androidx.compose.material.Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary
        ),
        modifier = modifier,
        onClick = onClick
    ) {
        content()
    }
}

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    text: String = "Section name",
    maxLines: Int = 1,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        content = {
            Text(
                style = Typography.button,
                maxLines = maxLines,
                text = text.uppercase()
            )
        }
    )
}

@Composable
fun ImageButton(
    modifier: Modifier = Modifier,
    text: String = "Section name",
    maxLines: Int = 1,
    imageVector: ImageVector = Icons.Default.BrokenImage,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        content = {
            Icon(
                imageVector = imageVector,
                contentDescription = null // Decorative
            )
            Spacer(modifier = Modifier
                .padding(4.dp)
            )
            Text(
                modifier = Modifier.weight(1.0f),
                style = Typography.button,
                maxLines = maxLines,
                text = text.uppercase()
            )
        }
    )
}

// region Previews

@Composable
@Preview(showBackground = true)
fun TextButtonOneLinePreview() {
    BrewdogTheme {
        TextButton(
            text = "Short section name",
            maxLines = 1
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TextButtonTwoLinePreview() {
    BrewdogTheme {
        TextButton(
            text = "Long\nsection name",
            maxLines = 2
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ImageButtonOneLinePreview() {
    BrewdogTheme {
        ImageButton(
            text = "Short section name",
            maxLines = 1
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ImageButtonTwoLinePreview() {
    BrewdogTheme {
        ImageButton(
            text = "Long\nsection name",
            maxLines = 2
        )
    }
}

// endregion Previews
