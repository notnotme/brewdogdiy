package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.ui.theme.Typography

val ListItemSize = 76.dp
val ListItemLargeSize = 125.dp

@Composable
fun ListItemError(
    modifier: Modifier = Modifier,
    text: String,
    onRetryClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            style = Typography.body1,
            text = text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        Button(
            onClick = { onRetryClick() }) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun ListItemLoading(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            style = Typography.body1,
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        CircularProgressIndicator()
    }
}

@Composable
fun ListItemPlaceHolder(
    height: Dp
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(height)
            .fillMaxWidth()) {
    }
}

// region Previews

@Composable
@Preview(showBackground = true)
fun ListItemLoadingPreview() {
    ListItemLoading(
        modifier = Modifier.height(ListItemLargeSize).fillMaxWidth().padding(16.dp),
        text = "Loading..."
    )
}

@Composable
@Preview(showBackground = true)
fun ListItemErrorPreview() {
    ListItemError(
        modifier = Modifier.height(ListItemLargeSize).fillMaxWidth().padding(16.dp),
        text = "Unknown error"
    ) {}
}

@Composable
@Preview(showBackground = true)
fun ListItemPlaceHolderPreview() {
    ListItemPlaceHolder(
        height = ListItemLargeSize
    )
}

// endregion Previews

