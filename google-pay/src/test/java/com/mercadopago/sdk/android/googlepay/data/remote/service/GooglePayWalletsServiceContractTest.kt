package com.mercadopago.sdk.android.googlepay.data.remote.service

import com.mercadopago.sdk.android.googlepay.di.googlePayRetrofitQualifier
import com.mercadopago.sdk.android.googlepay.di.provideNetworkModule
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import java.io.IOException

internal class GooglePayWalletsServiceContractTest {
    private val server = MockWebServer()

    @Before
    fun setUp() {
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun service(): GooglePayWalletsService {
        val retrofit: Retrofit = koinApplication {
            modules(module { includes(provideNetworkModule(server.url("/").toString())) })
        }.koin.get(googlePayRetrofitQualifier)
        return retrofit.create(GooglePayWalletsService::class.java)
    }

    @Test
    fun `when wallets is requested Then public key and product id travel only in the query`() {
        server.enqueue(MockResponse().setBody("[]"))

        runBlocking { service().getWallets(publicKey = "SESSION-KEY", productId = "PRODUCT") }

        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/bricks/wallets", request.requestUrl?.encodedPath)
        assertEquals("SESSION-KEY", request.requestUrl?.queryParameter("public_key"))
        assertEquals("PRODUCT", request.requestUrl?.queryParameter("product_id"))
        assertEquals(setOf("public_key", "product_id"), request.requestUrl?.queryParameterNames)
        assertFalse(request.headers.names().any { it.contains("key", true) || it.contains("product", true) })
        assertNull(request.getHeader("X-Public-Key"))
        assertNull(request.getHeader("X-Product-Id"))
    }

    @Test
    fun `when body has snake case fields Then it is parsed into the response`() {
        server.enqueue(
            MockResponse().setBody(
                """[{"code":"google_pay","supported_payment_methods":[{"id":"visa"},{"id":"master"}]}]""",
            ),
        )

        val response = runBlocking { service().getWallets("K", "P") }

        val wallet = response.body().orEmpty().single()
        assertEquals("google_pay", wallet.code)
        assertEquals(listOf("visa", "master"), wallet.supportedPaymentMethods?.map { it?.id })
    }

    @Test
    fun `when status is an error Then the response is not successful and keeps the status and body`() {
        server.enqueue(MockResponse().setResponseCode(404).setBody("""{"message":"Wallets not found"}"""))

        val response = runBlocking { service().getWallets("K", "P") }

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
        assertTrue(response.errorBody()?.string().orEmpty().contains("Wallets not found"))
    }

    @Test
    fun `when body is not the expected json Then parsing throws a non network exception`() {
        server.enqueue(MockResponse().setBody("""{"not":"a list"}"""))

        val failure = runCatching { runBlocking { service().getWallets("K", "P") } }.exceptionOrNull()

        assertNotNull(failure)
        assertFalse(failure is IOException)
    }
}
