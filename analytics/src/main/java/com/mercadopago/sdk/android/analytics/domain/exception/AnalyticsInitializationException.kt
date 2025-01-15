package com.mercadopago.sdk.android.analytics.domain.exception

class AnalyticsInitializationException(
    message: String = "Analytics is not initialized. Please start the Analytics first calling Analytics.initialize()",
) : Exception(message)
