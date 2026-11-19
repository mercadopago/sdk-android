package com.mercadopago.sdk.android.googlepay.di

import com.mercadopago.sdk.android.core.utils.PublicKeyStore
import com.mercadopago.sdk.android.googlepay.data.remote.service.GooglePayWalletsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal class GooglePayNetworkModuleTest {
    private interface ProbeService {
        @GET("probe")
        suspend fun call(
            @Query("public_key") publicKey: String,
            @Header("X-Public-Key") publicKeyHeader: String,
        ): okhttp3.ResponseBody

        @GET("probe")
        suspend fun callWithoutKey(): okhttp3.ResponseBody
    }

    private val server = MockWebServer()

    @Before
    fun setUp() {
        server.start()
    }

    @After
    fun tearDown() {
        PublicKeyStore.publicKey = null
        server.shutdown()
    }

    private fun retrofit(): Retrofit {
        val koin = koinApplication {
            modules(module { includes(provideNetworkModule(server.url("/").toString())) })
        }.koin
        return koin.get(googlePayRetrofitQualifier)
    }

    @Test
    fun `retrofit client has no interceptor at all, so no body logging`() {
        val client = retrofit().callFactory() as OkHttpClient

        assertTrue(client.interceptors.none { it is HttpLoggingInterceptor })
        assertTrue(client.networkInterceptors.none { it is HttpLoggingInterceptor })
        assertTrue(client.interceptors.isEmpty())
        assertTrue(client.networkInterceptors.isEmpty())
    }

    @Test
    fun `the client never retries a failed connection by itself, so a request is never sent twice`() {
        val client = retrofit().callFactory() as OkHttpClient

        assertFalse(client.retryOnConnectionFailure)
    }

    @Test
    fun `the wallets service uses the interceptor-free retrofit`() {
        val koin = koinApplication {
            modules(module { includes(provideNetworkModule(server.url("/").toString())) })
        }.koin
        val client = koin.get<Retrofit>(googlePayRetrofitQualifier).callFactory() as OkHttpClient

        assertNotNull(koin.get<GooglePayWalletsService>())
        assertTrue(client.interceptors.isEmpty() && client.networkInterceptors.isEmpty())
    }

    @Test
    fun `public key is only what the call passes, never read from the global store`() {
        PublicKeyStore.publicKey = "STORE-KEY"
        server.enqueue(MockResponse().setBody("{}"))
        server.enqueue(MockResponse().setBody("{}"))
        val service = retrofit().create(ProbeService::class.java)

        kotlinx.coroutines.runBlocking {
            service.call(publicKey = "SESSION-KEY", publicKeyHeader = "SESSION-KEY")
            service.callWithoutKey()
        }

        val explicit = server.takeRequest()
        assertEquals("SESSION-KEY", explicit.getHeader("X-Public-Key"))
        assertEquals("SESSION-KEY", explicit.requestUrl?.queryParameter("public_key"))
        val implicit = server.takeRequest()
        assertNull(implicit.getHeader("X-Public-Key"))
        assertNull(implicit.requestUrl?.queryParameter("public_key"))
        assertFalse(implicit.headers.names().any { it.contains("key", ignoreCase = true) })
    }
}
