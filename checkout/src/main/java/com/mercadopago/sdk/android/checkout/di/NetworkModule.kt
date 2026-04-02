package com.mercadopago.sdk.android.checkout.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

internal fun provideNetworkModule() =
    module {
        single {
            provideCardFormGson()
        }
    }

private fun provideCardFormGson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
}
