package com.mercadopago.sdk.android.analytics.data.datasource.remote

import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorDetailRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorSourceRequest
import com.mercadopago.sdk.android.analytics.data.remote.service.NativeErrorService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class NativeErrorNetworkIntegrationTest {
    private lateinit var server: MockWebServer
    private lateinit var dataSource: NativeErrorRemoteDataSource

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()
        val client = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .callTimeout(3, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
        val service = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(NativeErrorService::class.java)
        dataSource = NativeErrorRemoteDataSourceImpl(service)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `only 202 is accepted and request has no credential material`() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(202))

        assertTrue(dataSource.report(request()))
        val recorded = server.takeRequest()
        assertEquals("/op-frontend-metrics/v2/error-metric", recorded.path)
        assertEquals("POST", recorded.method)
        assertNull(recorded.getHeader("Authorization"))
        assertNull(recorded.getHeader("Cookie"))
        assertFalse(recorded.path.orEmpty().contains("public_key"))
        assertFalse(recorded.body.readUtf8().contains("public_key"))
    }

    @Test
    fun `non 202 redirect is rejected without retry`() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(302).setHeader("Location", "/other"))

        assertFalse(dataSource.report(request()))
        assertEquals(1, server.requestCount)
    }

    @Test
    fun `every non accepted status is contained without retry`() = runBlocking {
        listOf(200, 400, 401, 422, 429, 500, 503).forEach { status ->
            server.enqueue(MockResponse().setResponseCode(status))

            assertFalse(dataSource.report(request()), status.toString())
        }

        assertEquals(7, server.requestCount)
    }

    @Test
    fun `transport disconnect is contained without retry`() = runBlocking {
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))

        assertFalse(dataSource.report(request()))
        assertEquals(1, server.requestCount)
    }

    private fun request() = NativeErrorRequest(
        eventId = "3f6fd694-4ba8-4f45-ae7c-871c4698aace",
        occurredAt = "2026-08-27T12:00:00.000Z",
        source = NativeErrorSourceRequest(
            sdkName = "openplatform_sdk_android",
            sdkVersion = "1.0.0",
            hostPlatform = "android",
            sdkTechnology = "native",
            module = "core_methods",
            operation = "issuers",
        ),
        siteId = "MLA",
        error = NativeErrorDetailRequest(
            code = "request_timeout",
            category = "service",
            critical = true,
            statusCode = 504,
            requestCorrelationId = null,
            serviceTarget = "issuers",
            diagnosticCode = "timeout",
        ),
        device = null,
    )
}
