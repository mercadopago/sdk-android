package com.mercadopago.sdk.android.checkout.data.remote.request

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.domain.model.params.PaymentMethodType
import kotlinx.coroutines.CancellationException

private const val FEATURE_PAYMENT = "payment"
private const val PLATFORM_ANDROID = "android"

internal data class OrderProcessRequest(
    @SerializedName("payment_method_id")
    val paymentMethodId: String,
    @SerializedName("payment_method_type")
    val paymentMethodType: String,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("installments")
    val installments: Int? = null,
    @SerializedName("integration_data")
    val integrationData: IntegrationData? = null,
)

internal data class IntegrationData(
    @SerializedName("melidata_session_id")
    val melidataSessionId: String? = null,
    @SerializedName("feature")
    val feature: String? = null,
    @SerializedName("platform")
    val platform: String? = null,
    @SerializedName("app")
    val app: String? = null,
)

internal fun PaymentMethodType.toOrderProcessRequest(
    integrationData: IntegrationData? = null,
): OrderProcessRequest =
    when (this) {
        is PaymentMethodType.Card -> OrderProcessRequest(
            paymentMethodId = paymentMethodId,
            paymentMethodType = paymentMethodType,
            token = token,
            installments = installments,
            integrationData = integrationData,
        )

        is PaymentMethodType.Ticket -> OrderProcessRequest(
            paymentMethodId = paymentMethodId,
            paymentMethodType = PaymentMethodType.TICKET_TYPE,
            integrationData = integrationData,
        )
    }

internal suspend fun Context.toIntegrationData(): IntegrationData =
    IntegrationData(
        melidataSessionId = getMelidataSessionIdOrNull(),
        feature = FEATURE_PAYMENT,
        platform = PLATFORM_ANDROID,
        app = runCatching { applicationInfo?.packageName }
            .getOrNull()
            ?.takeIf(String::isNotEmpty),
    )

@Suppress("TooGenericExceptionCaught", "SwallowedException")
private suspend fun getMelidataSessionIdOrNull(): String? {
    val analytics = MPAnalytics.tryGetInstance() ?: return null
    return try {
        analytics.getSessionId().takeIf(String::isNotEmpty)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        null
    }
}
