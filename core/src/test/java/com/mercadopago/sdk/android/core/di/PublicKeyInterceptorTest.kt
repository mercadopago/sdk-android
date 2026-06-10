package com.mercadopago.sdk.android.core.di

import com.mercadopago.sdk.android.core.utils.interceptor.PublicKeyInterceptor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test

class PublicKeyInterceptorTest {

    private val chain = mockk<Interceptor.Chain>()
    private val mockResponse = mockk<Response>(relaxed = true)
    private val request = Request.Builder().url("http://myurl.com/teste").build()

    @Before
    fun setUp() {
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns mockResponse
    }

    @Test
    fun `should add public key to request URL`() {
        val publicKey = "teste_public_key"
        val interceptor = PublicKeyInterceptor { publicKey }

        interceptor.intercept(chain)

        val expectedUrl = "http://myurl.com/teste?public_key=$publicKey"
        verify { chain.proceed(match { it.url.toString() == expectedUrl }) }
    }

    @Test
    fun `given publicKey is null then proceeds without adding query param`() {
        val interceptor = PublicKeyInterceptor { null }

        interceptor.intercept(chain)

        verify { chain.proceed(match { it.url.queryParameter("public_key") == null }) }
    }

    @Test
    fun `given publicKey is empty then proceeds without adding query param`() {
        val interceptor = PublicKeyInterceptor { "" }

        interceptor.intercept(chain)

        verify { chain.proceed(match { it.url.queryParameter("public_key") == null }) }
    }
}
