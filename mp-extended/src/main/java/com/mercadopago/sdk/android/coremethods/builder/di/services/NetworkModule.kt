package com.mercadopago.sdk.android.coremethods.builder.di.services

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.coremethods.builder.data.remote.service.MPExtendedService
import org.koin.core.module.Module
import org.koin.dsl.module

private val snakeCaseGson = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .create()

internal fun provideNetworkModule(
    baseUrl: String,
): Module =
    module {
        single { RetrofitServiceFactory(baseUrl = baseUrl, publicKey = null, gson = snakeCaseGson) }
        single {
            get<RetrofitServiceFactory>().createService(MPExtendedService::class.java)
        }
    }
