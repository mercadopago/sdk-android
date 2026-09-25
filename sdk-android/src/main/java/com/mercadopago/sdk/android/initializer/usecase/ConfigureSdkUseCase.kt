package com.mercadopago.sdk.android.initializer.usecase

import android.content.Context
import android.util.Log
import com.mercadopago.sdk.android.BuildConfig
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDeliveryPolicy
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeObservabilityConfiguration
import com.mercadopago.sdk.android.analytics.observability.runtime.NativeErrorReporterProvider
import com.mercadopago.sdk.android.data.local.mapper.toSiteId
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.usecase.GetSiteIdUseCase
import com.mercadopago.sdk.android.domain.usecase.SetSiteIdUseCase
import com.mercadopago.sdk.android.initializer.analytics.SdkInitializerAnalytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal data class ConfigureSdkParams(
    val context: Context,
    val publicKey: String,
    val countryCode: CountryCode,
)

internal class ConfigureSdkUseCase(
    private val getSiteIdUseCase: GetSiteIdUseCase,
    private val setSiteIdUseCase: SetSiteIdUseCase,
) {

    fun configureObservability(params: ConfigureSdkParams) {
        NativeErrorReporterProvider.configure(
            context = params.context,
            configuration = NativeObservabilityConfiguration(
                sdkName = OPENPLATFORM_SDK_ANDROID,
                sdkVersion = BuildConfig.SdkVersion,
                siteId = params.countryCode.toSiteId(),
                deliveryPolicy = NativeErrorDeliveryPolicy(),
            ),
        )
    }

    operator fun invoke(params: ConfigureSdkParams): Flow<Unit> {
        MPAnalytics.initialize(
            context = params.context,
            getSiteIdFlow = getSiteIdUseCase(params.publicKey).map { siteId -> siteId.siteId },
        )
        return setSiteIdUseCase(params.publicKey, params.countryCode)
            .onEach {
                Log.d(TAG, "Reconfigured SDK")
                MPAnalytics.getInstance().trackMetric(
                    SdkInitializerAnalytics.buildSdkInitializerEvent(
                        context = params.context,
                        publicKey = params.publicKey,
                    )
                )
            }
            .catch { error ->
                Log.d(TAG, "Error reconfiguring SDK: ${error.message}", error)
            }
    }

    private companion object {
        private const val TAG: String = "ReconfigureSdkUseCase"
        private const val OPENPLATFORM_SDK_ANDROID = "openplatform_sdk_android"
    }
}
