@file:OptIn(DelicateCoroutinesApi::class)

package com.mercadopago.sdk.android.initializer

import android.content.Context
import android.util.Log
import androidx.annotation.RestrictTo
import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.di.MercadoPagoSdkModulesProvider
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.usecase.FetchSiteIdUseCase
import com.mercadopago.sdk.android.domain.usecase.GetSiteIdUseCase
import com.mercadopago.sdk.android.domain.usecase.SetSiteIdUseCase
import com.mercadopago.sdk.android.initializer.analytics.SdkInitializerAnalytics
import com.mercadopago.sdk.android.initializer.coroutines.SdkCoroutineProvider
import com.mercadopago.sdk.android.initializer.exceptions.EmptyPublicKeyException
import com.mercadopago.sdk.android.initializer.exceptions.SDKAlreadyInitializedException
import com.mercadopago.sdk.android.initializer.exceptions.SDKNotInitializedException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.Koin
import java.util.UUID

/**
 * Mercado Pago SDK. This class holds logic to initialize the SDK and call some of it's methods.
 *
 * @property koin The Koin instance used for dependency injection across the SDK.
 * @property publicKey The public key of your Mercado Pago account.
 * @property countryCode The country code associated with the public key.
 * @param sessionId The unique session identifier for this SDK instance.
 */
class MercadoPagoSDK internal constructor(
    val koin: Koin,
    internal val publicKey: String,
    internal val countryCode: CountryCode,
    private val sessionId: String,
) {

    /**
     * Companion object for the [MercadoPagoSDK] class.
     */
    companion object {
        private const val TAG = "MercadoPagoSDK"

        @Volatile
        private var sdkInstance: MercadoPagoSDK? = null

        /**
         * Check if the Mercado Pago SDK is initialized.
         */
        val isInitialized: Boolean
            get() = sdkInstance != null

        /**
         * Initializes the Mercado Pago SDK. This should be called before any other SDK method.
         * Call it inside the application class only once for your application.
         * @param context The application context.
         * @param publicKey The public key of your Mercado Pago account.
         * Must not be empty.
         * Please store this key safely on a secure place outside your app.
         * @param countryCode The country code associated with the [publicKey] of your Mercado Pago account.
         * It uses the ISO 3166-1 alpha-3 standard. The [countryCode] needs to match
         * the country code of the [publicKey] being used.
         * Use the [CountryCode] enum.
         * @see
         * <a href="https://www.mercadopago.com/developers/en/docs/your-integrations/credentials"
         * >Credentials Documentation</a>
         */
        @Synchronized
        fun initialize(
            context: Context,
            publicKey: String,
            countryCode: CountryCode,
        ) {
            if (sdkInstance != null) {
                throw SDKAlreadyInitializedException()
            }
            if (publicKey.isEmpty()) {
                throw EmptyPublicKeyException()
            }
            val modulesProvider = MercadoPagoSdkModulesProvider(
                publicKey = publicKey,
                context = context,
            )
            val sessionId = UUID.randomUUID().toString()
            sdkInstance = MercadoPagoSDK(
                koin = modulesProvider.koinApp,
                sessionId = sessionId,
                publicKey = publicKey,
                countryCode = countryCode,
            )
            SdkCoroutineProvider.provideSDKCoroutineScope().launch {
                val fetchSiteIdUseCase = modulesProvider.koinApp.get<FetchSiteIdUseCase>()
                val getSiteIdUseCase = modulesProvider.koinApp.get<GetSiteIdUseCase>()
                val setSiteIdUseCase = modulesProvider.koinApp.get<SetSiteIdUseCase>()
                setSiteIdUseCase(publicKey, countryCode).firstOrNull()
                DeviceSDK.getInstance().execute(context)
                MPAnalytics.initialize(
                    context = context,
                    getSiteIdFlow = getSiteIdUseCase(publicKey).map { siteId ->
                        siteId.siteId
                    },
                )
                fetchSiteIdUseCase(publicKey)
                    .catch { error ->
                        Log.d(TAG, "Error initializing SDK: ${error.message}", error)
                        MPAnalytics.getInstance().trackMetric(
                            SdkInitializerAnalytics.buildSdkInitializerEvent(
                                context = context,
                                publicKey = publicKey,
                                errorType = "Error initializing SDK: ${error.message}",
                            )
                        )
                    }
                    .collect { siteId ->
                        Log.d(TAG, "Initialized SDK")
                        MPAnalytics.getInstance().trackMetric(
                            SdkInitializerAnalytics.buildSdkInitializerEvent(
                                context = context,
                                publicKey = publicKey,
                            )
                        )
                    }
            }
        }

        /**
         * Get the current instance of the MercadoPagoSDK.
         * This should be called after the SDK is initialized.
         * @return The current instance of the MercadoPagoSDK.
         */
        fun getInstance(): MercadoPagoSDK {
            return sdkInstance ?: throw SDKNotInitializedException()
        }

        /**
         * @suppress
         * Only for internal usage. DO NOT USE IN PRODUCTION.
         * Clear the current instance of the MercadoPagoSDK for testing purposes.
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        fun clearInstance() {
            SdkCoroutineProvider.provideSDKCoroutineScope().launch {
                sdkInstance?.koin?.close()
                sdkInstance = null
            }
        }

        /**
         * @suppress
         * Check the current public key
         */
        val publicKey: String?
            @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
            get() = sdkInstance?.publicKey

        /**
         * @suppress
         * Check the current country code
         */
        val countryCode: CountryCode?
            @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
            get() = sdkInstance?.countryCode
    }
}
