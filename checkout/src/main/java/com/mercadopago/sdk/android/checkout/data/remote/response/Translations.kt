package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class Translations(
    val cardFormTitle: String,
    val cardNumber: CardNumberTranslations,
    val holderName: HolderNameTranslations,
    val expirationDate: ExpirationDateTranslations,
    val securityCode: SecurityCodeTranslations,
    val document: DocumentTranslations,
    val installments: InstallmentsTranslations,
)
