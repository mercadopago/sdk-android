package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class CardFormInitResponse(
    val identificationTypes: List<IdentificationType>,
    val cardNumber: CardNumberConfig,
    val securityCode: SecurityCodeConfig,
    val holderName: HolderNameConfig,
    val expirationDate: ExpirationDateConfig,
    val translations: Translations,
)
