package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.R

@Composable
fun SimpleAppBar(
    title: String = stringResource(R.string.app_name),
    backAction: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        elevation = 0.dp,
        title = {
            Text(
                text = title
            )
        },
        navigationIcon = if (backAction != null) {
            {
                IconButton(
                    onClick = backAction
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.navigate_back)
                    )
                }
            }
        } else null,
        actions = actions
    )
}

// region Previews

@Composable
@Preview(showBackground = false)
fun SimpleAppBarPreview() {
    SimpleAppBar()
}

@Composable
@Preview(showBackground = false)
fun SimpleAppBarWithNavigationIcon() {
    SimpleAppBar(
        title = "Details of something"
    ) { }
}

@Composable
@Preview(showBackground = false)
fun SimpleAppBarWithAction() {
    SimpleAppBar(
        title = "Details of something",
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.FilterList, contentDescription = "")
            }
        }
    )
}

@Composable
@Preview(showBackground = false)
fun SimpleAppBarWithNavigationIconAndAction() {
    SimpleAppBar(
        title = "Details of something",
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.FilterList, contentDescription = "")
            }
        },
        backAction = { }
    )
}

// endregion Previews
