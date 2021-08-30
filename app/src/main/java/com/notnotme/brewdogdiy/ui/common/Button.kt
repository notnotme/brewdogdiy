package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme
import com.notnotme.brewdogdiy.ui.theme.Typography

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
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = MaterialTheme.colors.secondary
        ),
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
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = MaterialTheme.colors.secondary
        ),
        content = {
            Icon(
                imageVector = imageVector,
                contentDescription = null // Decorative
            )
            Spacer(
                modifier = Modifier
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

@Composable
fun CheckableTextButton(
    modifier: Modifier = Modifier,
    text: String = "Label",
    maxLines: Int = 1,
    checked: Boolean,
    uncheckedBackgroundColor: Color = MaterialTheme.colors.surface,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = if (checked) {
            ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = contentColorFor(uncheckedBackgroundColor)
            )
        } else {
            ButtonDefaults.outlinedButtonColors(
                backgroundColor = uncheckedBackgroundColor,
                contentColor = contentColorFor(uncheckedBackgroundColor)
            )
        },
        border = if (checked) {
            null
        } else {
            BorderStroke(1.dp, MaterialTheme.colors.secondary)
        },
        content = {
            Text(
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
fun CheckableTextButtonCheckedPreview() {
    BrewdogTheme {
        CheckableTextButton(
            checked = true
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CheckableTextButtonNotCheckedPreview() {
    BrewdogTheme {
        CheckableTextButton(
            checked = false
        )
    }
}

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
fun ImageButtonWithBorderOneLinePreview() {
    BrewdogTheme {
        ImageButton(
            text = "Short section name",
            maxLines = 1
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
