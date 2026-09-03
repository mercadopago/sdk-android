package com.mercadopago.sdk.android.analytics.di

import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.analytics.data.datasource.remote.NativeErrorRemoteDataSource
import com.mercadopago.sdk.android.analytics.data.datasource.remote.NativeErrorRemoteDataSourceImpl
import com.mercadopago.sdk.android.analytics.data.remote.mapper.NativeErrorRequestMapper
import com.mercadopago.sdk.android.analytics.data.remote.service.NativeErrorService
import com.mercadopago.sdk.android.analytics.data.repository.NativeErrorRepositoryImpl
import com.mercadopago.sdk.android.analytics.domain.interactor.MPErrorReporter
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDeliveryMode
import com.mercadopago.sdk.android.analytics.domain.repository.NativeErrorRepository
import com.mercadopago.sdk.android.analytics.domain.usecase.ReportNativeErrorUseCase
import com.mercadopago.sdk.android.core.BuildConfig
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal const val NATIVE_ERROR_GSON = "native_error_gson"
internal const val NATIVE_ERROR_HTTP_CLIENT = "native_error_http_client"
internal const val NATIVE_ERROR_RETROFIT = "native_error_retrofit"
private const val TRANSPORT_TIMEOUT_SECONDS = 2L
private const val CALL_TIMEOUT_SECONDS = 3L

internal fun provideReporterModule(nativeSiteId: String) = module {
    single(named(NATIVE_ERROR_GSON)) { GsonBuilder().create() }
    single(named(NATIVE_ERROR_HTTP_CLIENT)) {
        OkHttpClient.Builder()
            .connectTimeout(TRANSPORT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TRANSPORT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TRANSPORT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
    }
    single(named(NATIVE_ERROR_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.MERCADO_PAGO_API_URL)
            .client(get(named(NATIVE_ERROR_HTTP_CLIENT)))
            .addConverterFactory(GsonConverterFactory.create(get(named(NATIVE_ERROR_GSON))))
            .build()
    }
    single {
        get<Retrofit>(named(NATIVE_ERROR_RETROFIT)).create(NativeErrorService::class.java)
    }
    factory { NativeErrorRequestMapper(androidApplication(), nativeSiteId) }
    factory<NativeErrorRemoteDataSource> { NativeErrorRemoteDataSourceImpl(get()) }
    factory<NativeErrorRepository> { NativeErrorRepositoryImpl(get(), get()) }
    factory { ReportNativeErrorUseCase(get()) }
    single {
        NativeErrorDeliveryMode.from(
            com.mercadopago.sdk.android.analytics.BuildConfig.NATIVE_ERROR_DELIVERY_MODE
        )
    }
    single {
        MPErrorReporter(
            reportNativeError = get(),
            deliveryMode = get(),
        )
    }
}
