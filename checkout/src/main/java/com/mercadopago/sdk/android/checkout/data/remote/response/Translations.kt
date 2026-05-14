package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class Translations(
    val cardFormTitle: String,
    val cardFormFooterButtonLabel: String,
    val currencySymbol: String = "",
    val cardNumber: FieldTranslations,
    val holderName: FieldTranslations,
    val expirationDate: FieldTranslations,
    val securityCode: SecurityCodeTranslations,
    val document: DocumentTranslations,
    val installments: InstallmentsTranslations,
)
