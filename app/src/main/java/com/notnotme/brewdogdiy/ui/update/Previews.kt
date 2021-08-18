package com.notnotme.brewdogdiy.ui.update

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import java.util.*

@Composable
@Preview(showBackground = true)
fun UpdateScreenWhenUpdating() {
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

@Composable
@Preview(showBackground = true)
fun UpdateScreenWhenUpdatedWithBackAction() {
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

@Composable
@Preview(showBackground = true)
fun UpdateScreenWhenError() {
    UpdateScreen(
        updating = false,
        downloadStatus = null,
        errorMessage = "Device offline",
        onUpdateButtonClick = {}
    )
}
