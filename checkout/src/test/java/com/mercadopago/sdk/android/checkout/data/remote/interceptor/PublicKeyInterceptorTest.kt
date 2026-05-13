package com.mercadopago.sdk.android.checkout.data.remote.interceptor

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PublicKeyInterceptorTest {
    private val chain = mockk<Interceptor.Chain>()
    private val mockResponse = mockk<Response>(relaxed = true)
    private val request = Request.Builder().url("https://api.mercadopago.com/test").build()

    @Before
    fun setUp() {
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns mockResponse
    }

    @Test
    fun `given publicKey is null then proceeds without X-Public-Key header`() {
        val interceptor = PublicKeyInterceptor { null }

        interceptor.intercept(chain)

        verify { chain.proceed(match { it.header("X-Public-Key") == null }) }
    }

    @Test
    fun `given publicKey is empty then proceeds without X-Public-Key header`() {
        val interceptor = PublicKeyInterceptor { "" }

        interceptor.intercept(chain)

        verify { chain.proceed(match { it.header("X-Public-Key") == null }) }
    }

    @Test
    fun `given publicKey is provided then adds X-Public-Key header to request`() {
        val publicKey = "APP_USR-123456"
        val interceptor = PublicKeyInterceptor { publicKey }

        interceptor.intercept(chain)

        verify { chain.proceed(match { it.header("X-Public-Key") == publicKey }) }
    }

    @Test
    fun `given publicKey is provided then returns response from chain`() {
        val interceptor = PublicKeyInterceptor { "APP_USR-123456" }

        val result = interceptor.intercept(chain)

        assertEquals(mockResponse, result)
    }

    @Test
    fun `given publicKey is null then returns response from chain`() {
        val interceptor = PublicKeyInterceptor { null }

        val result = interceptor.intercept(chain)

        assertEquals(mockResponse, result)
    }
}
