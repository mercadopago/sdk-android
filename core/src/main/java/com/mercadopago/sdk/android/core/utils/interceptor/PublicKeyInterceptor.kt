package com.mercadopago.sdk.android.core.utils.interceptor

import okhttp3.Interceptor
import okhttp3.Response

private const val PublicKey = "public_key"

internal class PublicKeyInterceptor(
    private val publicKey: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder().apply {
            addQueryParameter(PublicKey, publicKey)
        }.build()
        val newRequest = request.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}
