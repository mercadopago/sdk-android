package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.core.utils.KoverIgnore

@KoverIgnore("in development")
internal fun MercadoPagoCheckoutError.toErrorTypeString(): String =
    when (this) {
        is MercadoPagoCheckoutError.NetworkError -> "network_error"
        is MercadoPagoCheckoutError.ServiceError -> "service_error"
        is MercadoPagoCheckoutError.ConfigurationError -> "integration_error"
        is MercadoPagoCheckoutError.UnknownError -> "unknown_error"
    }
