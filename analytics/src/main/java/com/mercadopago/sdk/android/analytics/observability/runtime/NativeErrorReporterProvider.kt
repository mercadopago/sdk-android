package com.mercadopago.sdk.android.analytics.observability.runtime

import android.content.Context
import androidx.annotation.RestrictTo
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.analytics.observability.data.datasource.remote.FrontendMetricRemoteDataSourceImpl
import com.mercadopago.sdk.android.analytics.observability.data.remote.mapper.NativeErrorRequestMapper
import com.mercadopago.sdk.android.analytics.observability.data.remote.service.FrontendMetricService
import com.mercadopago.sdk.android.analytics.observability.data.repository.FrontendMetricRepositoryImpl
import com.mercadopago.sdk.android.analytics.observability.domain.interactor.MPErrorReporter
import com.mercadopago.sdk.android.analytics.observability.domain.interactor.NativeErrorReporting
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeObservabilityConfiguration
import com.mercadopago.sdk.android.analytics.observability.domain.usecase.ReportNativeErrorUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Owns the process-local reporter lifecycle independently from legacy MPAnalytics.
 * Each configuration atomically replaces and closes the previous immutable reporter.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object NativeErrorReporterProvider {
    @Volatile
    private var reporter: NativeErrorReporting? = null

    /** Replaces the active reporter using [configuration] and the application [context]. */
    @Synchronized
    fun configure(
        context: Context,
        configuration: NativeObservabilityConfiguration,
    ) {
        val nextReporter = createReporter(context.applicationContext, configuration)
        val previousReporter = reporter
        reporter = nextReporter
        previousReporter?.close()
    }

    /** Returns the configured reporter, or `null` before SDK initialization. */
    fun getOrNull(): NativeErrorReporting? = reporter

    /** Removes and closes the active reporter. */
    @Synchronized
    fun clear() {
        val previousReporter = reporter
        reporter = null
        previousReporter?.close()
    }

    private fun createReporter(
        context: Context,
        configuration: NativeObservabilityConfiguration,
    ): NativeErrorReporting {
        val client = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
        val service = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(FrontendMetricService::class.java)
        val mapper = NativeErrorRequestMapper(context, configuration)
        val dataSource = FrontendMetricRemoteDataSourceImpl(service, mapper)
        val repository = FrontendMetricRepositoryImpl(dataSource)
        return MPErrorReporter(
            reportNativeError = ReportNativeErrorUseCase(repository),
            deliveryPolicy = configuration.deliveryPolicy,
        )
    }

    private const val BASE_URL = "https://api.mercadopago.com/"
    private const val CONNECT_TIMEOUT_SECONDS = 2L
    private const val READ_TIMEOUT_SECONDS = 2L
    private const val WRITE_TIMEOUT_SECONDS = 2L
    private const val CALL_TIMEOUT_SECONDS = 3L
}
