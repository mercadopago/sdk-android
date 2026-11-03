package com.mercadopago.sdk.android.checkout.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

private const val PRODUCT_ID_QUERY_KEY = "product_id"

internal class ProductIdInterceptor(
    private val productIdProvider: () -> String,
) : Interceptor {
    override fun intercept(
        chain: Interceptor.Chain,
    ): Response {
        val request = chain.request()

        val newUrl = request.url.newBuilder()
            .setQueryParameter(PRODUCT_ID_QUERY_KEY, productIdProvider())
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
