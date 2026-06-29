@file:OptIn(DelicateCoroutinesApi::class)

package com.mercadopago.sdk.android.initializer

import android.content.Context
import androidx.annotation.RestrictTo
import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.core.utils.PublicKeyStore
import com.mercadopago.sdk.android.di.MercadoPagoSdkModulesProvider
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.initializer.coroutines.SdkCoroutineProvider
import com.mercadopago.sdk.android.initializer.exceptions.EmptyPublicKeyException
import com.mercadopago.sdk.android.initializer.exceptions.SDKAlreadyInitializedException
import com.mercadopago.sdk.android.initializer.exceptions.SDKNotInitializedException
import com.mercadopago.sdk.android.initializer.usecase.ConfigureSdkParams
import com.mercadopago.sdk.android.initializer.usecase.ConfigureSdkUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
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
 * @param applicationContext The application context
 */
class MercadoPagoSDK private constructor(
    val koin: Koin,
    internal var publicKey: String,
    internal var countryCode: CountryCode,
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    internal val sessionId: String,
    private val applicationContext: Context,
) {

    /**
     * Companion object for the [MercadoPagoSDK] class.
     */
    companion object {

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
                context = context.applicationContext,
            )
            val sessionId = UUID.randomUUID().toString()
            sdkInstance = MercadoPagoSDK(
                koin = modulesProvider.koinApp,
                sessionId = sessionId,
                publicKey = publicKey,
                countryCode = countryCode,
                applicationContext = context.applicationContext,
            )
            PublicKeyStore.publicKey = publicKey
            SdkCoroutineProvider.provideSDKCoroutineScope().launch {
                val instance = getInstance()
                DeviceSDK.getInstance().execute(instance.applicationContext)
                val configureSdkUseCase: ConfigureSdkUseCase = modulesProvider.koinApp.get()
                configureSdkUseCase(
                    ConfigureSdkParams(
                        context = context,
                        publicKey = publicKey,
                        countryCode = countryCode,
                    )
                ).collect { _ -> }
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
         * Updates the current SDK configuration with a new public key and country code.
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
        fun setNewConfiguration(
            publicKey: String,
            countryCode: CountryCode,
        ) {
            val instance: MercadoPagoSDK = sdkInstance ?: throw SDKNotInitializedException()
            if (publicKey.isEmpty()) {
                throw EmptyPublicKeyException()
            }
            if (instance.publicKey == publicKey && instance.countryCode == countryCode) {
                return
            }
            val modulesProvider: MercadoPagoSdkModulesProvider = MercadoPagoSdkModulesProvider(
                publicKey = publicKey,
                context = instance.applicationContext,
            )
            instance.publicKey = publicKey
            instance.countryCode = countryCode
            PublicKeyStore.publicKey = publicKey

            SdkCoroutineProvider.provideSDKCoroutineScope().launch {
                val configureSdkUseCase: ConfigureSdkUseCase = modulesProvider.koinApp.get()
                configureSdkUseCase(
                    ConfigureSdkParams(
                        context = instance.applicationContext,
                        publicKey = publicKey,
                        countryCode = countryCode,
                    )
                ).collect { _ -> }
            }
        }

        /**
         * @suppress
         * Only for internal usage. DO NOT USE IN PRODUCTION.
         * Clear the current instance of the MercadoPagoSDK for testing purposes.
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        fun clearInstance() {
            val instanceToClose = sdkInstance
            sdkInstance = null
            SdkCoroutineProvider.provideSDKCoroutineScope().launch {
                instanceToClose?.koin?.close()
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
