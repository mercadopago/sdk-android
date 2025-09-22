package com.mercadopago.sdk.android.coremethods.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class CardTokenResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("public_key")
    val publicKey: String? = null,
    @SerializedName("card_id")
    val cardId: String? = null,
    @SerializedName("luhnValidation")
    val luhnValidation: Boolean? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("dateUsed")
    val dateUsed: String? = null,
    @SerializedName("date_created")
    val dateCreated: String? = null,
    @SerializedName("date_last_updated")
    val dateLastUpdated: String? = null,
    @SerializedName("date_due")
    val dateDue: String? = null,
    @SerializedName("cardNumberLength")
    val cardNumberLength: Int? = null,
    @SerializedName("truncCardNumber")
    val truncCardNumber: String? = null,
    @SerializedName("securityCodeLength")
    val securityCodeLength: Int? = null,
    @SerializedName("expiration_month")
    val expirationMonth: Int? = null,
    @SerializedName("expiration_year")
    val expirationYear: Int? = null,
    @SerializedName("first_six_digits")
    val firstSixDigits: String? = null,
    @SerializedName("last_four_digits")
    val lastFourDigits: String? = null,
    @SerializedName("liveMode")
    val liveMode: Boolean? = null,
    @SerializedName("require_esc")
    val requireEsc: Boolean? = null,
    @SerializedName("cardholder")
    val cardholder: CardHolderResponse? = null,
    @SerializedName("esc")
    val esc: String? = null,
)

internal data class CardHolderResponse(
    @SerializedName("identification")
    val identification: IdentificationResponse? = null,
    @SerializedName("name")
    val name: String? = null,
)

internal data class IdentificationResponse(
    @SerializedName("number")
    val number: String? = null,
    @SerializedName("type")
    val type: String? = null,
)
