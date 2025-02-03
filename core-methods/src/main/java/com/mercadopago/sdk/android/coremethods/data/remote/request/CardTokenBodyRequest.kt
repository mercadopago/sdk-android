package com.mercadopago.sdk.android.coremethods.data.remote.request

import com.google.gson.annotations.SerializedName

internal data class CardTokenBodyRequest(
    @SerializedName("cardId")
    val cardId: String? = null,
    @SerializedName("esc")
    val esc: String? = null,
    @SerializedName("requireEsc")
    val requireEsc: Boolean = false,
    @SerializedName("card_number")
    val cardNumber: String? = null,
    @SerializedName("security_code")
    val securityCode: String? = null,
    @SerializedName("expiration_month")
    val expirationMonth: Int? = null,
    @SerializedName("expiration_year")
    val expirationYear: Int? = null,
    @SerializedName("cardholder")
    val buyerIdentification: BuyerIdentificationBodyRequest? = null,
)

internal data class BuyerIdentificationBodyRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("type")
    val type: String,
)
