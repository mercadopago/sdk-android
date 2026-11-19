package com.mercadopago.sdk.android.googlepay.domain.model

/**
 * Purchase data.
 *
 * @property totalPrice integer or with 2 decimals, dot separator. Ex. "100.00".
 * @property currencyCode ISO 4217. Ex. "BRL". `null` = derived from the SDK site.
 * @property countryCode ISO 3166-1 alpha-2. `null` = derived from the SDK country.
 */
data class MPGooglePayTransaction(
    val totalPrice: String,
    val currencyCode: String? = null,
    val countryCode: String? = null,
)
