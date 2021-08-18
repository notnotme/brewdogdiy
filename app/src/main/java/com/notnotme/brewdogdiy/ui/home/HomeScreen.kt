package com.notnotme.brewdogdiy.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.ui.common.SimpleAppBar
import com.notnotme.brewdogdiy.ui.theme.Typography

@Composable
fun HomeScreen(
    buttonListClicked: () -> Unit,
    buttonRandomClicked: () -> Unit,
    onUpdateClick: () -> Unit,
) {
    Scaffold(
        topBar = { SimpleAppBar(
            actions = {
                IconButton(onClick = { onUpdateClick() }) {
                    Icon(Icons.Default.Sync, contentDescription = stringResource(R.string.update))
                }
            }
        ) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
    }
}
