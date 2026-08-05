package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class MethodSelectionScreenResponse(
    @SerializedName("header_title") val headerTitle: String,
    @SerializedName("selection_type") val selectionType: String,
    @SerializedName("footer") val footer: MethodSelectionScreenFooterResponse? = null,
    @SerializedName("options") val options: List<MethodSelectionOptionResponse>? = null,
)

internal data class MethodSelectionScreenFooterResponse(
    @SerializedName("total_label") val totalLabel: String,
    @SerializedName("total_amount") val totalAmount: String,
    @SerializedName("button") val button: MethodSelectionScreenButtonResponse? = null,
)

internal data class MethodSelectionScreenButtonResponse(
    @SerializedName("label") val label: String,
)

internal data class MethodSelectionOptionResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("subtitle") val subtitle: String? = null,
    @SerializedName("icon_url") val iconUrl: String? = null,
)
