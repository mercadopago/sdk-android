package com.mercadopago.sdk.android.coremethods.domain.model.params

import com.mercadolibre.android.device.sdk.domain.Device

internal data class GenerateCardTokenParams(
    val cardId: String? = null,
    val esc: String? = null,
    val requireEsc: Boolean = false,
    val cardNumber: String? = null,
    val securityCode: String? = null,
    val expirationMonth: Int? = null,
    val expirationYear: Int? = null,
    val buyerIdentification: BuyerIdentificationParam? = null,
    val device: Device? = null,
)

internal data class BuyerIdentificationParam(
    val name: String?,
    val number: String?,
    val type: String?,
)
