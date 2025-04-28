package com.mercadopago.sdk.android.core.di

import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.utils.interceptor.PublicKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit Factory
 * Use this class to create a new retrofit service.
 * @param publicKey The seller's public key.
 * @param baseUrl The base url of the api.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class RetrofitServiceFactory(
    private val publicKey: String?,
    private val baseUrl: String,
) {

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        OkHttpClient.Builder().apply {
            if (publicKey != null) {
                addInterceptor(PublicKeyInterceptor(publicKey))
            }
            addInterceptor(loggingInterceptor)
        }.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Create a new service in retrofit
     * @param serviceClass a java class of a retrofit service
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
