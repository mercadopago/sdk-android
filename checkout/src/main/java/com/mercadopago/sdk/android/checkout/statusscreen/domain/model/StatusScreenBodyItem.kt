package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

internal sealed class StatusScreenBodyItem {
    data class ListItem(
        val title: String?,
        val subtitle: String?,
        val imageUrl: String?,
        val leadingType: String?,
        val leadingValue: String?,
        val leadingIcon: StatusScreenIcon? = null,
        val style: StatusScreenListItemStyle? = null,
        val subtitleSegments: List<StatusScreenSubtitleSegment> = emptyList(),
    ) : StatusScreenBodyItem()

    data class Message(
        val text: String,
    ) : StatusScreenBodyItem()

    data class Barcode(
        val content: String,
        val codeFormatted: String,
        val copyLabel: String,
        val copyFeedback: String,
        val imageDataUri: String?,
        val label: String?,
        val icon: StatusScreenIcon?,
    ) : StatusScreenBodyItem()
}

internal enum class StatusScreenIcon {
    Copy,
    Bill,
}

internal enum class StatusScreenListItemStyle {
    Simple,
}
