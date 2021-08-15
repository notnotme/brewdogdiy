package com.notnotme.brewdogdiy

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class BeerDataStoreTest {

    private val appModule = ApplicationModule()

    private val dataStore = appModule.provideBeerDataStore(
        InstrumentationRegistry.getInstrumentation().targetContext,
        inMemory = true
    )

    private val beerDao = dataStore.beerDao()
    private val updateDao = dataStore.updateDao()

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dataStore.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_write_read_delete_beers() {
        val beers = mutableListOf<Beer>()
        for (i in 0..4) {
            beers.add(
                Beer(
                    id = i.toLong(),
                    name = "Beer test",
                    tagLine = "This is just a test",
                    imageUrl = null,
                    abv = Math.random().toFloat(),
                    description = "Bla bla...",
                    firstBrewed = Date(),
                    contributedBy = "notnotme"
                )
            )
        }

        runBlocking {
            val saved = beerDao.saveBeers(beers)
            assertEquals(saved.size, 5)

            val byId = beerDao.getBeer(1L).first()
            Assert.assertEquals(byId, beers[1])
            Assert.assertNotEquals(byId, beers[0])
            Assert.assertNotEquals(byId, beers[2])
            Assert.assertNotEquals(byId, beers[3])
            Assert.assertNotEquals(byId, beers[4])

            val deleted = beerDao.deleteBeers()
            assertEquals(deleted, 5)
            val byIdDeleted = beerDao.getBeer(1L).first()
            Assert.assertEquals(byIdDeleted, null)
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_write_and_read_download_status() {
        val downloadStatus = DownloadStatus(
            id = 1L,
            totalBeers = 1337,
            lastUpdate = Date(),
            isFinished = false,
            page = 3
        )

        runBlocking {
            val id = updateDao.saveDownloadStatus(downloadStatus)
            assertEquals(id, 1L)

            val byId = updateDao.getDownloadStatus(1L)
            Assert.assertEquals(byId, downloadStatus)

            val idDeleted = updateDao.deleteDownloadStatus(downloadStatus.id)
            assertEquals(idDeleted, 1)

            val byIdDeleted = updateDao.getDownloadStatus(1L)
            Assert.assertEquals(byIdDeleted, null)
        }
    }

}