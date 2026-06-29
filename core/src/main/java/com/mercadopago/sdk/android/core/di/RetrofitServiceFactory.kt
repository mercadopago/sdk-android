package com.mercadopago.sdk.android.core.di

import androidx.annotation.RestrictTo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.utils.PublicKeyStore
import com.mercadopago.sdk.android.core.utils.interceptor.PublicKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Factory class for creating Retrofit service instances with standardized configuration.
 * This class provides a centralized way to create Retrofit services with consistent
 * network configuration, including public key authentication and logging.
 *
 * The factory configures OkHttpClient with appropriate interceptors and creates
 * Retrofit instances with Gson conversion for JSON handling.
 *
 * @param publicKey The seller's public key for API authentication
 * @param baseUrl The base URL for the API endpoints
 * @param gson The base Gson for the retrofit converter factory
 *
 * Example:
 * ```kotlin
 * // Create a factory instance
 * val factory = RetrofitServiceFactory(
 *     publicKey = "YOUR_PUBLIC_KEY",
 *     baseUrl = "https://api.mercadopago.com/"
 * )
 *
 * // Create a service instance
 * val paymentService = factory.createService(PaymentService::class.java)
 * ```
 *
 * @see PublicKeyInterceptor
 * @see HttpLoggingInterceptor
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class RetrofitServiceFactory @JvmOverloads constructor(
    private val publicKey: String?,
    private val baseUrl: String,
    private val gson: Gson = GsonBuilder().create()
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
            addInterceptor(PublicKeyInterceptor { PublicKeyStore.publicKey ?: publicKey })
            addInterceptor(loggingInterceptor)
        }.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Creates a new Retrofit service instance for the specified service class.
     * This method generates a service implementation that can be used to make
     * API calls according to the service interface definition.
     *
     * @param serviceClass The Java class of the Retrofit service interface
     * @return A new instance of the specified service class
     *
     * Example:
     * ```kotlin
     * // Define a service interface
     * interface PaymentService {
     *     @GET("payments")
     *     suspend fun getPayments(): List<Payment>
     * }
     *
     * // Create and use the service
     * val paymentService = factory.createService(PaymentService::class.java)
     * val payments = paymentService.getPayments()
     * ```
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    /**
     * Used just in mp_extended removed
     * @param gson: Gson used in retrofit
     */
    @Deprecated("Will be replaced in new versions")
    fun withGson(gson: Gson): RetrofitServiceFactory =
        RetrofitServiceFactory(publicKey = publicKey, baseUrl = baseUrl, gson = gson)
}
