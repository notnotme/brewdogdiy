package com.notnotme.brewdogdiy

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

/**
 * Enqueue a response to a MockWebServer
 * The next call to a webservice that use this mocked server will receive the content
 * of the file as body response and the code as status code.
 *
 * @param file The file to read
 * @param code The status code of the queued response
 */
internal fun MockWebServer.enqueueResponse(file: String, code: Int) {
    val response = javaClass.classLoader?.getResource(file)?.readText(Charsets.UTF_8)
        ?: throw Exception(file)

    enqueue(
        MockResponse()
            .setResponseCode(code)
            .setBody(response)
    )
}
