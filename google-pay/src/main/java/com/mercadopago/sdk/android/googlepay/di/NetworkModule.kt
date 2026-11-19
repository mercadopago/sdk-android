package com.mercadopago.sdk.android.googlepay.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.googlepay.data.remote.service.GooglePayWalletsService
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal val googlePayRetrofitQualifier = named("googlePay")

internal fun provideNetworkModule(
    baseUrl: String = BuildConfig.MERCADO_PAGO_API_URL,
): Module =
    module {
        single(googlePayRetrofitQualifier) {
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkHttpClient.Builder().retryOnConnectionFailure(false).build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        single<GooglePayWalletsService> {
            get<Retrofit>(googlePayRetrofitQualifier).create(GooglePayWalletsService::class.java)
        }
    }
