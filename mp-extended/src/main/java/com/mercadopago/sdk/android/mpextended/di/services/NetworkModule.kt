package com.mercadopago.sdk.android.mpextended.di.services

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.utils.PublicKeyStore
import com.mercadopago.sdk.android.mpextended.data.remote.interceptor.PublicKeyInterceptor
import com.mercadopago.sdk.android.mpextended.data.remote.service.MPExtendedService
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val mpExtendedQualifier = named("mpExtended")

internal fun provideNetworkModule(): Module =
    module {
        single(mpExtendedQualifier) {
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(PublicKeyInterceptor { PublicKeyStore.publicKey })
                .build()
            Retrofit.Builder()
                .baseUrl(BuildConfig.MERCADO_PAGO_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        single { get<Retrofit>(mpExtendedQualifier).create(MPExtendedService::class.java) }
    }
