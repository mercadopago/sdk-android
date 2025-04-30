package com.mercadopago.sdk.android.coremethods.data.remote.request

import com.google.gson.annotations.SerializedName
import com.mercadolibre.android.device.sdk.domain.Device

internal data class CardTokenBodyRequest(
    @SerializedName("cardId")
    val cardId: String? = null,
    @SerializedName("esc")
    val esc: String? = null,
    @SerializedName("requireEsc")
    val requireEsc: Boolean = false,
    @SerializedName("card_number")
    val cardNumber: String? = null,
    @SerializedName("cardholder")
    val buyerIdentification: BuyerIdentificationBodyRequest? = null,
    @SerializedName("device")
    val device: Device? = null,
    @SerializedName("expiration_month")
    val expirationMonth: Int? = null,
    @SerializedName("expiration_year")
    val expirationYear: Int? = null,
    @SerializedName("security_code")
    val securityCode: String? = null,
)

internal data class BuyerIdentificationBodyRequest(
    @SerializedName("name")
    val name: String?,
    @SerializedName("identification")
    val identification: BuyerDocumentIdentificationBodyRequest?
)

internal data class BuyerDocumentIdentificationBodyRequest(
    @SerializedName("number")
    val number: String?,
    @SerializedName("type")
    val type: String?,
)
