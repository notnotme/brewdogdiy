package com.notnotme.brewdogdiy

import com.notnotme.brewdogdiy.repository.ApiDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ApiServiceTest {

    private val appModule = ApplicationModule()

    private val mockWebServer = MockWebServer()

    private val dataSource = ApiDataSource(
        appModule.provideApiService(
            appModule.provideRetrofit(
                appModule.provideOkHttpClient(),
                mockWebServer.url("/").toString()
            )
        )
    )

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun test_fetch_beer_list_proper() {
        mockWebServer.enqueueResponse("get_beers_proper.json", 200)

        runBlocking {
            val result = dataSource.getBeers(1, 25)
            assertEquals(result.isSuccessful, true)
            assertNotNull(result.body())
            assertEquals(result.body()!!.size, 25)
        }
    }

    @Test
    fun test_fetch_search_result_error() {
        mockWebServer.enqueueResponse("get_beers_error.json", 404)

        runBlocking {
            val result = dataSource.getBeers(0, 25)
            assertEquals(result.isSuccessful, false)
            assertEquals(result.body(), null)
            assert(!result.message().isNullOrBlank())
        }
    }

    @Test
    fun test_fetch_search_result_broken() {
        mockWebServer.enqueueResponse("get_beers_broken.json", 200)

        runBlocking {
            try {
                dataSource.getBeers(0, 25)
            } catch (exception: Exception) {
                assertNotNull(exception)
            }
        }
    }

}