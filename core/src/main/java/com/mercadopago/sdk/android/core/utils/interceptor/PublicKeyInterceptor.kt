package com.mercadopago.sdk.android.core.utils.interceptor

import androidx.annotation.RestrictTo
import okhttp3.Interceptor
import okhttp3.Response

private const val PUBLIC_KEY = "public_key"
internal const val PUBLIC_KEY_HEADER = "X-Public-Key"

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class PublicKeyInterceptor(
    private val publicKeyProvider: () -> String?,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val currentPublicKey: String? = publicKeyProvider()
        if (currentPublicKey.isNullOrEmpty()) {
            return chain.proceed(request)
        }
        return if (request.header(PUBLIC_KEY_HEADER) != null) {
            val newRequest = request.newBuilder()
                .removeHeader(PUBLIC_KEY_HEADER)
                .addHeader(PUBLIC_KEY_HEADER, currentPublicKey)
                .build()

            chain.proceed(newRequest)
        } else {
            val url = request.url.newBuilder()
                .addQueryParameter(PUBLIC_KEY, currentPublicKey)
                .build()

            chain.proceed(request.newBuilder().url(url).build())
        }
    }
}
