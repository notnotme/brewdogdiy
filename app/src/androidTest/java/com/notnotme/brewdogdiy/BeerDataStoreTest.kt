package com.notnotme.brewdogdiy

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.notnotme.brewdogdiy.model.domain.Beer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
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

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dataStore.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_write_and_read_beer() {
        val domainBeer = Beer(
            id = 1337L,
            name = "Beer test",
            tagLine = "This is just a test",
            imageUrl = null,
            abv = 0.4f,
            description = "Bla bla...",
            firstBrewed = Date(),
            contributedBy = "notnotme"
        )

        runBlocking {
            beerDao.insertBeer(domainBeer)
            val byId = beerDao.getBeer(1337L).first()
            assertThat(byId, equalTo(domainBeer))
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_write_and_read_beers() {
        val beers = mutableListOf<Beer>()
        for (i in 0..5) {
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
            beerDao.insertBeers(beers)
            val byId = beerDao.getBeer(1L).first()
            Assert.assertEquals(byId, beers[1])
            Assert.assertNotEquals(byId, beers[0])
            Assert.assertNotEquals(byId, beers[2])
            Assert.assertNotEquals(byId, beers[3])
            Assert.assertNotEquals(byId, beers[4])
        }
    }

}