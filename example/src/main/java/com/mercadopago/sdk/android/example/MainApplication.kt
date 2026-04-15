package com.mercadopago.sdk.android.example

import android.app.Application
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.example.di.providePresentationModule
import com.mercadopago.sdk.android.example.domain.model.PublicKey
import com.mercadopago.sdk.android.example.extensions.formatPublicKey
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.PUBLIC_KEY.isNotEmpty()) {
            val countryCode = getCountryCodeFromPublicKey(BuildConfig.PUBLIC_KEY)
            MercadoPagoSDK.initialize(
                context = this,
                publicKey = BuildConfig.PUBLIC_KEY,
                countryCode = countryCode,
            )
        }
        initializeKoin()
    }

    private fun getCountryCodeFromPublicKey(publicKey: String): CountryCode {
        return try {
            val publicKeyList = Json.decodeFromString<List<PublicKey>>(
                BuildConfig.DEFAULT_PUBLIC_KEY_LIST.formatPublicKey()
            )
            val matchedKey = publicKeyList.find { it.publicKey == publicKey }
            when (matchedKey?.countryCode) {
                "BRA" -> CountryCode.BRA
                "ARG" -> CountryCode.ARG
                "MEX" -> CountryCode.MEX
                "CHL" -> CountryCode.CHL
                "COL" -> CountryCode.COL
                "PER" -> CountryCode.PER
                "URY" -> CountryCode.URY
                else -> CountryCode.ARG
            }
        } catch (e: Exception) {
            CountryCode.ARG
        }
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(this@MainApplication)
            modules(
                modules = listOf(
                    providePresentationModule(this@MainApplication),
                ),
            )
        }
    }
}
