package com.mercadopago.sdk.android.mpextended.di.services

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.core.di.RetrofitServiceFactory
import com.mercadopago.sdk.android.mpextended.data.remote.service.MPExtendedService
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val mpExtendedQualifier = named("mpExtended")

internal fun provideNetworkModule(): Module =
    module {
        single(mpExtendedQualifier) {
            get<RetrofitServiceFactory>().withGson(
                GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create(),
            )
        }
        single { get<RetrofitServiceFactory>(mpExtendedQualifier).createService(MPExtendedService::class.java) }
    }
