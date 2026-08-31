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

internal class ProductIdInterceptorTest {
    private val chain = mockk<Interceptor.Chain>()
    private val mockResponse = mockk<Response>(relaxed = true)
    private val request = Request.Builder().url("https://api.mercadopago.com/test").build()

    @Before
    fun setUp() {
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns mockResponse
    }

    @Test
    fun `given productId is provided then adds product_id query param to request`() {
        val productId = "CVQP49FTT60D1548Q56G"
        val interceptor = ProductIdInterceptor { productId }

        interceptor.intercept(chain)

        verify {
            chain.proceed(
                match { it.url.queryParameter("product_id") == productId },
            )
        }
    }

    @Test
    fun `given request already has product_id query param then replaces it`() {
        val requestWithProductId = Request.Builder()
            .url("https://api.mercadopago.com/test?product_id=OLD_VALUE")
            .build()
        every { chain.request() } returns requestWithProductId
        val productId = "NEW_VALUE"
        val interceptor = ProductIdInterceptor { productId }

        interceptor.intercept(chain)

        verify {
            chain.proceed(
                match { it.url.queryParameterValues("product_id") == listOf(productId) },
            )
        }
    }

    @Test
    fun `given productId is provided then returns response from chain`() {
        val interceptor = ProductIdInterceptor { "CVQP49FTT60D1548Q56G" }

        val result = interceptor.intercept(chain)

        assertEquals(mockResponse, result)
    }
}
