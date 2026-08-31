package com.mercadopago.sdk.android.checkout.domain.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

internal data class ReviewConfirmFooter(
    @SerializedName("button") val button: ReviewConfirmButton,
    @SerializedName("currency_symbol") val currencySymbol: String?,
    @SerializedName("total_amount") val totalAmount: BigDecimal,
    @SerializedName("total_label") val totalLabel: String?,
    @SerializedName("installments") val installments: ReviewConfirmInstallments?,
    @SerializedName("description") val description: String?,
    @SerializedName("interest_label") val interestLabel: String?,
)

internal data class ReviewConfirmButton(
    @SerializedName("label") val label: String,
)

internal data class ReviewConfirmInstallments(
    @SerializedName("label") val label: String,
    @SerializedName("secondary_label") val secondaryLabel: String?,
    @SerializedName("state") val state: String?,
)
