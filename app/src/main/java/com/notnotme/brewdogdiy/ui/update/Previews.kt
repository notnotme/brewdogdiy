package com.notnotme.brewdogdiy.ui.update

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme
import java.util.*

@Composable
@Preview(showBackground = true)
fun UpdateScreenWhenUpdating() {
    BrewdogTheme {
        UpdateScreen(
            updating = true,
            downloadStatus = DownloadStatus(
                id = 0L,
                lastUpdate = Date(),
                totalBeers = 84
            ),
            errorMessage = null,
            onUpdateButtonClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun UpdateScreenWhenUpdatedWithBackAction() {
    BrewdogTheme {
        UpdateScreen(
            updating = false,
            downloadStatus = DownloadStatus(
                id = 0L,
                lastUpdate = Date(),
                totalBeers = 84
            ),
            errorMessage = null,
            onUpdateButtonClick = {},
            backAction = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun UpdateScreenWhenError() {
    BrewdogTheme {
        UpdateScreen(
            updating = false,
            downloadStatus = null,
            errorMessage = "Device offline",
            onUpdateButtonClick = {}
        )
    }
}
