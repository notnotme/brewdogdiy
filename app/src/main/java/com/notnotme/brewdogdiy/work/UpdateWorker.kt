package com.notnotme.brewdogdiy.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.GsonBuilder
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import com.notnotme.brewdogdiy.repository.BeerRepository
import com.notnotme.brewdogdiy.util.contentOrNull
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

@HiltWorker
class UpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val beerRepository: BeerRepository
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "UpdateWorker"

        /** Key to map the Worker result with */
        const val KEY_RESULT = "result"

        /** Key to get the Worker progress */
        const val KEY_UPDATE = "update"

        /** Key to get the Worker failure */
        const val KEY_FAILURE = "failure"

        /** Download beers by chunks of XX */
        const val DOWNLOAD_BATCH_SIZE = 80

        /** Helper to convert DownloadStatus from and to String */
        private val gson = GsonBuilder().create()

        /** @return A String representation of the download status */
        fun DownloadStatus.toJsonString(): String = gson.toJson(this)

        /** @return A DownloadStatus according the the String content */
        fun String.toDownloadStatus(): DownloadStatus? = gson.fromJson(this, DownloadStatus::class.java)
    }

    /**
     * Keep an instance of DownloadStatus as cache
     */
    private val downloadStatus = DownloadStatus(
        id = 1L,
        lastUpdate = Date(System.currentTimeMillis()),
        totalBeers = 0
    )

    /**
     * When doWork is called the process of updating the database will begin.
     * We just loop page after page collecting all beers that the api give to us
     * until no more beers are received.
     * When each batch of beers is received, the objects are converted to domain model
     * and saved to the database.
     * When the process is finished, the last DownloadStatus is saved to database, this object
     * tells that the dataset is complete.
     */
    override suspend fun doWork(): Result {
        Log.d(TAG, "Start...")
        resetDownloadStatus()

        Log.d(TAG, "Delete old data...")
        beerRepository.runInTransaction {
            beerRepository.deleteBeers()
            beerRepository.deleteDownloadStatus()
        }

        Log.d(TAG, "Download... (batch size: $DOWNLOAD_BATCH_SIZE)")
        var currentPage = 1
        var isFinished = false
        while (!isFinished) {
            Log.d(TAG, "Loop: $downloadStatus")
            try {

                val response = beerRepository.getBeersFromRemote(currentPage, DOWNLOAD_BATCH_SIZE)
                val body = response.body()
                if (!response.isSuccessful || body == null) {
                    error(response.message().contentOrNull() ?: "Unknown error")
                }

                beerRepository.runInTransaction {
                    // This can return less object than we passed initally,
                    // "bad" objects will be skipped and a log line is printed.
                    val savedCount = beerRepository.saveBeersToDao(body)
                    downloadStatus.totalBeers += savedCount.size
                    Log.d(TAG, "Saving ${savedCount.size} beers")

                    if (body.size < DOWNLOAD_BATCH_SIZE) {
                        isFinished = true
                    } else {
                        currentPage += 1
                    }
                }

                val progress = workDataOf(
                    KEY_UPDATE to downloadStatus.toJsonString()
                )
                setProgress(progress)

            } catch (exception: Exception) {
                val error = exception.message?.contentOrNull() ?: "Unknown error"
                val failure = workDataOf(
                    KEY_FAILURE to error
                )
                return Result.failure(failure)
            }
        }

        Log.d(TAG, "Finalize...")
        beerRepository.runInTransaction {
            beerRepository.saveDownloadStatus(downloadStatus)
        }

        Log.d(TAG, "Finished")
        val success = workDataOf(
            KEY_RESULT to downloadStatus.toJsonString()
        )
        return Result.success(success)
    }

    /** Reset the cached DownloadStatus */
    private fun resetDownloadStatus() {
        downloadStatus.apply {
            lastUpdate = Date(System.currentTimeMillis())
            totalBeers = 0
        }
    }

}