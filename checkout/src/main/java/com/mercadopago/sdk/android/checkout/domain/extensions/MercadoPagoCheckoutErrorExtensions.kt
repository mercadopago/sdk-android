package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

internal const val ANALYTICS_ERROR_NETWORK = "network_error"
internal const val ANALYTICS_ERROR_SERVICE = "service_error"
internal const val ANALYTICS_ERROR_UNKNOWN = "unknown_error"

internal fun MercadoPagoCheckoutError.toAnalyticsErrorType(): String =
    when (this) {
        is MercadoPagoCheckoutError.NetworkError -> ANALYTICS_ERROR_NETWORK
        is MercadoPagoCheckoutError.ServiceError -> ANALYTICS_ERROR_SERVICE
        else -> ANALYTICS_ERROR_UNKNOWN
    }
