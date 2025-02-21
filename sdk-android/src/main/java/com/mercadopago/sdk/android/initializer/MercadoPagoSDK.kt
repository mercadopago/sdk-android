@file:OptIn(DelicateCoroutinesApi::class)

package com.mercadopago.sdk.android.initializer

import android.content.Context
import android.util.Log
import com.mercadopago.sdk.android.BuildConfig
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.di.MercadoPagoSdkModulesProvider
import com.mercadopago.sdk.android.domain.usecase.FetchSiteIdUseCase
import com.mercadopago.sdk.android.domain.usecase.GetSiteIdUseCase
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH
import com.mercadopago.sdk.android.initializer.analytics.buildSdkInitializerEventData
import com.mercadopago.sdk.android.initializer.exceptions.SDKAlreadyInitializedException
import com.mercadopago.sdk.android.initializer.exceptions.SDKNotInitializedException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.Koin
import java.util.UUID

/**
 * Mercado Pago SDK. This class holds logic to initialize the SDK and call some of it's methods.
 */
class MercadoPagoSDK private constructor(
    internal val koin: Koin,
    private val sessionId: String,
) {

    companion object {
        private const val TAG = "MercadoPagoSDK"

        @Volatile
        private var sdkInstance: MercadoPagoSDK? = null

        /**
         * Initializes the Mercado Pago SDK. This should be called before any other SDK method.
         * Call it inside the application class only once for your application.
         * @param context The application context.
         * @param publicKey The public key of your Mercado Pago account.
         * Please store this key safely on a secure place outside your app.
         * Read here for more info:
         * @see [Credentials](https://www.mercadopago.com/developers/en/docs/your-integrations/credentials)
         */
        fun initialize(
            context: Context,
            publicKey: String,
        ) {
            if (sdkInstance != null) {
                throw SDKAlreadyInitializedException()
            }
            GlobalScope.launch(Dispatchers.IO) {
                val modulesProvider = MercadoPagoSdkModulesProvider(
                    publicKey = publicKey,
                    context = context,
                )
                val fetchSiteIdUseCase = modulesProvider.koinApp.get<FetchSiteIdUseCase>()
                val getSiteIdUseCase = modulesProvider.koinApp.get<GetSiteIdUseCase>()
                val sessionId = UUID.randomUUID().toString()
                sdkInstance = MercadoPagoSDK(
                    koin = modulesProvider.koinApp,
                    sessionId = sessionId,
                )
                MPAnalytics.initialize(
                    sessionId = sessionId,
                    publicKey = publicKey,
                    version = BuildConfig.SdkVersion,
                    getSiteIdFlow = getSiteIdUseCase(publicKey).map { siteId ->
                        siteId.siteId
                    },
                )
                fetchSiteIdUseCase(publicKey)
                    .catch { error ->
                        Log.d(TAG, "Error initializing SDK: ${error.message}", error)
                        MPAnalytics.getInstance().trackMetric(
                            Metric(
                                type = TrackType.EVENT,
                                path = SDK_NATIVE_PATH,
                                data = buildSdkInitializerEventData(context),
                            )
                        )
                    }
                    .collect { siteId ->
                        MPAnalytics.getInstance().trackMetric(
                            Metric(
                                type = TrackType.EVENT,
                                path = SDK_NATIVE_PATH,
                                data = buildSdkInitializerEventData(context),
                            )
                        )
                        Log.d(TAG, "Initialized SDK")
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
    }
}
