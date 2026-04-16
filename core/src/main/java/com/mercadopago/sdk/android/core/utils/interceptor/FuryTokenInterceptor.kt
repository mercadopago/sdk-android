package com.mercadopago.sdk.android.core.utils.interceptor

import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.utils.FuryTokenStore
import okhttp3.Interceptor
import okhttp3.Response

private const val FURY_TEST_URL = "beta--bricks-api.furyapps.io"
private const val TIGER_TOKEN_HEADER = "X-Tiger-Token"

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class FuryTokenInterceptor(
    private val baseUrl: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = FuryTokenStore.token
        if (!BuildConfig.DEBUG || !baseUrl.contains(FURY_TEST_URL) || token.isNullOrEmpty()) {
            return chain.proceed(chain.request())
        }
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader(TIGER_TOKEN_HEADER, "Bearer $token")
                .build(),
        )
    }
}
