package com.mercadopago.sdk.android.core.di

import com.mercadopago.sdk.android.core.utils.interceptor.PublicKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test

class PublicKeyInterceptorTest {

    @Test
    fun `should add public key to request URL`() {
        val url = "http://myurl.com/teste"
        val publicKey = "teste_public_key"

        val originalRequest = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(PublicKeyInterceptor { publicKey })
            .build()

        val newResponse = client.newCall(originalRequest).execute()

        val expectedUrl = "$url?public_key=$publicKey"
        assertEquals(expectedUrl, newResponse.request.url.toString())
    }
}
