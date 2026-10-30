package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.domain.extensions.mapAllOrNull
import com.mercadopago.sdk.android.checkout.domain.extensions.orNullIfBlank
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyDataResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyNodeResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenButtonResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenFooterResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenHeaderResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenSubtitleSegmentResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooter
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenHeader
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSubtitleSegment
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData

private const val COMPONENT_LIST_ITEM = "MPListItem"
private const val COMPONENT_BARCODE = "MPBarcode"
private const val COMPONENT_MESSAGE = "MPMessage"

internal fun StatusScreenResponse.toViewDataOrNull(): StatusScreenViewData? {
    val safeStatusType = StatusType.entries.find { it.wireValue == statusType }
    val safeHeader = header?.toDomainOrNull()
    val safeFooter = footer?.toDomainOrNull()
    if (safeStatusType == null || safeHeader == null || safeFooter == null) return null
    return StatusScreenViewData(
        statusType = safeStatusType.wireValue,
        header = safeHeader,
        body = body.orEmpty().mapNotNull { it.toDomainOrNull() },
        footer = safeFooter,
        canRetry = canRetry,
    )
}

private fun StatusScreenHeaderResponse.toDomainOrNull(): StatusScreenHeader? =
    title.orNullIfBlank()?.let { safeTitle ->
        icon.asHttpsUrlOrNull()?.let { safeIcon ->
            StatusScreenHeader(title = safeTitle, iconUrl = safeIcon, subtitle = subtitle.orNullIfBlank())
        }
    }

private fun StatusScreenBodyNodeResponse.toDomainOrNull(): StatusScreenBodyItem? =
    when (component) {
        COMPONENT_LIST_ITEM -> data?.toListItem()
        COMPONENT_BARCODE -> data?.toBarcodeOrNull()
        COMPONENT_MESSAGE -> data?.text.orNullIfBlank()?.let { StatusScreenBodyItem.Message(text = it) }
        else -> null
    }

private fun StatusScreenBodyDataResponse.toListItem(): StatusScreenBodyItem.ListItem {
    val segments = subtitleSegments.orEmpty().mapNotNull { it.toDomainOrNull() }
    return StatusScreenBodyItem.ListItem(
        title = title.orNullIfBlank(),
        subtitle = subtitle ?: segments.takeIf { it.isNotEmpty() }?.joinToString(separator = "") { it.text },
        imageUrl = imageUrl.asHttpsUrlOrNull(),
        leadingType = leadingType,
        leadingValue = leadingValue,
        leadingIcon = toLeadingIconOrNull(),
        style = style.toListItemStyleOrNull(),
        subtitleSegments = segments,
    )
}

private fun StatusScreenSubtitleSegmentResponse.toDomainOrNull(): StatusScreenSubtitleSegment? =
    text?.let { StatusScreenSubtitleSegment(text = it, state = state.toSegmentStateOrNull()) }

private fun StatusScreenBodyDataResponse.toBarcodeOrNull(): StatusScreenBodyItem.Barcode? {
    val safeFields = listOf(content, codeFormatted, copyLabel, copyFeedback).mapAllOrNull { it.orNullIfBlank() }
        ?: return null
    return StatusScreenBodyItem.Barcode(
        content = safeFields[0],
        codeFormatted = safeFields[1],
        copyLabel = safeFields[2],
        copyFeedback = safeFields[3],
        imageDataUri = imageDataUri.asBarcodeImageDataUriOrNull(),
        label = label.orNullIfBlank(),
        icon = icon.toStatusScreenIconOrNull(),
    )
}

private fun StatusScreenFooterResponse.toDomainOrNull(): StatusScreenFooter? =
    buttons?.mapAllOrNull { it.toDomainOrNull() }
        ?.let { safeButtons -> StatusScreenFooter(buttons = safeButtons) }

private fun StatusScreenButtonResponse.toDomainOrNull(): StatusScreenFooterButton? =
    label.orNullIfBlank()?.let { safeLabel ->
        val safeStyle = style.toStatusScreenButtonStyleOrNull()
        when (FooterButtonAction.entries.find { it.wireValue == action }) {
            FooterButtonAction.BACK -> StatusScreenFooterButton.Back(label = safeLabel, style = safeStyle)
            FooterButtonAction.OPEN_PDF -> (receiptUrl ?: value).asHttpsUrlOrNull()?.let { safeReceiptUrl ->
                StatusScreenFooterButton.OpenPdf(label = safeLabel, receiptUrl = safeReceiptUrl, style = safeStyle)
            }
            FooterButtonAction.CHANGE_PAYMENT_METHOD ->
                StatusScreenFooterButton.ChangePaymentMethod(label = safeLabel, style = safeStyle)
            null -> null
        }
    }
