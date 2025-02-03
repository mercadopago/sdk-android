package com.mercadopago.sdk.android.core.utils.interceptor

import okhttp3.Interceptor
import okhttp3.Response

private const val PUBLIC_KEY = "public_key"

internal class PublicKeyInterceptor(
    private val publicKey: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder().apply {
            addQueryParameter(PUBLIC_KEY, publicKey)
        }.build()
        val newRequest = request.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}
