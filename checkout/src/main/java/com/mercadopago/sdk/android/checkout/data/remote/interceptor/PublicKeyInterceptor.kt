package com.mercadopago.sdk.android.checkout.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

private const val PUBLIC_KEY = "X-Public-Key"

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

        val newRequest = request.newBuilder()
            .addHeader(PUBLIC_KEY, currentPublicKey)
            .build()

        return chain.proceed(newRequest)
    }
}
