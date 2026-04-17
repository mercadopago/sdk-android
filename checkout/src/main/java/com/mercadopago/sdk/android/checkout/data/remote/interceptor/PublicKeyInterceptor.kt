package com.mercadopago.sdk.android.checkout.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

private const val PUBLIC_KEY = "public_key"

internal class PublicKeyInterceptor(
    private val publicKeyProvider: () -> String?,
) : Interceptor {
    override fun intercept(
        chain: Interceptor.Chain,
    ): Response {
        val request = chain.request()
        val currentPublicKey: String? = publicKeyProvider()
        if (currentPublicKey.isNullOrEmpty()) {
            return chain.proceed(request)
        }
        val url = request.url.newBuilder().apply {
            addQueryParameter(PUBLIC_KEY, currentPublicKey)
        }.build()
        val newRequest = request.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}
