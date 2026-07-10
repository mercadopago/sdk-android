package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

internal data class CardFormInitResponse(
    @SerializedName("identification_types")
    val identificationTypes: List<IdentificationType>,
    @SerializedName("card_number")
    val cardNumber: CardNumberConfig,
    @SerializedName("security_code")
    val securityCode: SecurityCodeConfig,
    @SerializedName("holder_name")
    val holderName: HolderNameConfig,
    @SerializedName("expiration_date")
    val expirationDate: ExpirationDateConfig,
    @SerializedName("translations")
    val translations: Translations,
    @SerializedName("amount")
    val amount: BigDecimal? = null,
)
