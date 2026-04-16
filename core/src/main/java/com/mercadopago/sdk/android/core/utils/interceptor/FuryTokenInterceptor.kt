package com.mercadopago.sdk.android.core.utils.interceptor

import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.utils.FuryTokenStore
import okhttp3.Interceptor
import okhttp3.Response

private const val FURY_TEST_URL = "beta--bricks-api.furyapps.io"
private const val TIGER_TOKEN_HEADER = "X-Tiger-Token"
private const val TIGER_PUBLIC_HEADER = "X-Public-Key"

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class FuryTokenInterceptor(
    private val baseUrl: String,
    private val publicKey: String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = FuryTokenStore.token?.takeIf { it.isNotEmpty() } ?: BuildConfig.FURY_TOKEN
        val key = publicKey ?: ""
        if (!BuildConfig.DEBUG || !baseUrl.contains(FURY_TEST_URL) || token.isEmpty() || key.isEmpty()) {
            return chain.proceed(chain.request())
        }
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader(TIGER_TOKEN_HEADER, "Bearer $token")
                .addHeader(TIGER_PUBLIC_HEADER, key)
                .build(),
        )
    }
}
