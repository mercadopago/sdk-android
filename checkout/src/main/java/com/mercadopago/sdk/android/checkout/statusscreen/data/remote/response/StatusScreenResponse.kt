package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class StatusScreenResponse(
    @SerializedName("status_type")
    val statusType: String?,
    @SerializedName("header")
    val header: StatusScreenHeaderResponse?,
    @SerializedName("body")
    val body: List<StatusScreenBodyNodeResponse>?,
    @SerializedName("footer")
    val footer: StatusScreenFooterResponse?,
    @SerializedName("can_retry")
    val canRetry: Boolean? = null,
)

internal data class StatusScreenHeaderResponse(
    @SerializedName("title")
    val title: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("subtitle")
    val subtitle: String? = null,
)

internal data class StatusScreenBodyNodeResponse(
    @SerializedName("component")
    val component: String?,
    @SerializedName("data")
    val data: StatusScreenBodyDataResponse?,
)

internal data class StatusScreenBodyDataResponse(
    @SerializedName("title")
    val title: String?,
    @SerializedName("subtitle")
    val subtitle: String?,
    @SerializedName("subtitle_segments")
    val subtitleSegments: List<StatusScreenSubtitleSegmentResponse>? = null,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("leading_type")
    val leadingType: String?,
    @SerializedName("leading_value")
    val leadingValue: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("code_formatted")
    val codeFormatted: String?,
    @SerializedName("copy_label")
    val copyLabel: String?,
    @SerializedName("copy_feedback")
    val copyFeedback: String?,
    @SerializedName("image_data_uri")
    val imageDataUri: String?,
    @SerializedName("label")
    val label: String? = null,
    @SerializedName("icon")
    val icon: String? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("style")
    val style: String? = null,
)

internal data class StatusScreenSubtitleSegmentResponse(
    @SerializedName("text")
    val text: String?,
    @SerializedName("state")
    val state: String? = null,
)

internal data class StatusScreenFooterResponse(
    @SerializedName("buttons")
    val buttons: List<StatusScreenButtonResponse>?,
)

internal data class StatusScreenButtonResponse(
    @SerializedName("label")
    val label: String?,
    @SerializedName("action")
    val action: String?,
    @SerializedName("receipt_url")
    val receiptUrl: String?,
    @SerializedName("value")
    val value: String? = null,
    @SerializedName("style")
    val style: String? = null,
)
