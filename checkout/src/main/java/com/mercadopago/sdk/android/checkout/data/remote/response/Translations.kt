package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class Translations(
    @SerializedName("card_form_title")
    val cardFormTitle: String,
    @SerializedName("card_form_footer_button_label")
    val cardFormFooterButtonLabel: String,
    @SerializedName("currency_symbol")
    val currencySymbol: String = "",
    @SerializedName("card_number")
    val cardNumber: FieldTranslations,
    @SerializedName("holder_name")
    val holderName: FieldTranslations,
    @SerializedName("expiration_date")
    val expirationDate: FieldTranslations,
    @SerializedName("security_code")
    val securityCode: SecurityCodeTranslations,
    @SerializedName("document")
    val document: DocumentTranslations,
    @SerializedName("installments")
    val installments: InstallmentsTranslations,
)
