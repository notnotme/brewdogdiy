package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme
import com.notnotme.brewdogdiy.ui.theme.Typography

@Composable
fun SectionButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.BrokenImage,
    text: String = "Section name",
    maxLines: Int = 1,
    color: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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
    }
}

// region Previews

@Composable
@Preview(showBackground = true)
fun SectionButtonOneLinePreview() {
    BrewdogTheme {
        SectionButton(
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SectionButtonTwoLinePreview() {
    BrewdogTheme {
        SectionButton(
            text = "Long\nsection name",
            maxLines = 2
        )
    }
}

// endregion Previews
