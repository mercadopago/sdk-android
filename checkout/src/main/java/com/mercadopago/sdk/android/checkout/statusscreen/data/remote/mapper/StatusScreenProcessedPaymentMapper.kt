package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.domain.extensions.orNullIfBlank
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.ORDER_STATUS_ACTION_REQUIRED
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.ORDER_STATUS_PENDING
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.ORDER_STATUS_REJECTED
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.PAYMENT_STATUS_ERROR
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.PAYMENT_STATUS_REJECTED
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.STATUS_FAILED
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenButtonStyle
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFallbackCopy
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooter
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenHeader
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenIcon
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.methodDisplayName

internal const val STATUS_TYPE_FALLBACK = "fallback"

private val CARD_PAYMENT_TYPES = setOf("credit_card", "debit_card", "prepaid_card")

internal fun StatusScreenFallbackCopy.toStatusScreenViewData(
    payment: OrderProcessOutput.PaymentProcessed?,
    lastFourDigits: String?,
    orderStatus: String? = null,
): StatusScreenViewData {
    val barcode = payment?.toBarcode(this)
    val kind = fallbackKindFor(payment, orderStatus)
    return StatusScreenViewData(
        statusType = STATUS_TYPE_FALLBACK,
        header = headerFor(kind),
        body = when (kind) {
            FallbackKind.Error -> emptyList()
            FallbackKind.Pending -> listOfNotNull(barcode)
            FallbackKind.Ticket -> listOfNotNull(barcode)
            FallbackKind.Rejected, FallbackKind.Generic -> listOfNotNull(
                payment?.takeIf { it.isCardPayment() }?.toPaymentRow(lastFourDigits, paymentSubtitle)
                    .takeIf { barcode == null },
                barcode,
            )
        },
        footer = StatusScreenFooter(buttons = listOfNotNull(payment?.toOpenUrlButton(this), backButton())),
    )
}

private enum class FallbackKind { Rejected, Error, Ticket, Pending, Generic }

private fun fallbackKindFor(
    payment: OrderProcessOutput.PaymentProcessed?,
    orderStatus: String?,
): FallbackKind =
    when {
        payment?.status == PAYMENT_STATUS_REJECTED || orderStatus == ORDER_STATUS_REJECTED -> FallbackKind.Rejected
        payment?.status == PAYMENT_STATUS_ERROR || payment?.status == STATUS_FAILED || orderStatus == STATUS_FAILED ->
            FallbackKind.Error
        isActionRequired(payment, orderStatus) -> FallbackKind.Ticket
        orderStatus in ORDER_STATUS_PENDING -> FallbackKind.Pending
        else -> FallbackKind.Generic
    }

private fun isActionRequired(
    payment: OrderProcessOutput.PaymentProcessed?,
    orderStatus: String?,
): Boolean = orderStatus == ORDER_STATUS_ACTION_REQUIRED || payment?.status == ORDER_STATUS_ACTION_REQUIRED

private fun StatusScreenFallbackCopy.headerFor(
    kind: FallbackKind,
): StatusScreenHeader =
    when (kind) {
        FallbackKind.Rejected -> rejectedHeader
        FallbackKind.Error -> errorHeader
        FallbackKind.Ticket -> StatusScreenHeader(title = ticketTitle, iconRes = iconRes)
        FallbackKind.Pending -> pendingHeader
        FallbackKind.Generic -> StatusScreenHeader(title = title, iconRes = iconRes)
    }

private fun StatusScreenFallbackCopy.backButton(): StatusScreenFooterButton =
    StatusScreenFooterButton.Back(label = backLabel, style = StatusScreenButtonStyle.Transparent)

private fun OrderProcessOutput.PaymentProcessed.toOpenUrlButton(
    copy: StatusScreenFallbackCopy,
): StatusScreenFooterButton? =
    (paymentMethod?.ticketUrl.asHttpsUrlOrNull() ?: paymentMethod?.redirectUrl.asHttpsUrlOrNull())?.let { url ->
        StatusScreenFooterButton.OpenUrl(label = copy.openUrlLabel, url = url, style = StatusScreenButtonStyle.Loud)
    }

private fun OrderProcessOutput.PaymentProcessed.toBarcode(
    copy: StatusScreenFallbackCopy,
): StatusScreenBodyItem.Barcode? =
    paymentMethod?.barcodeContent.orNullIfBlank()?.let { content ->
        StatusScreenBodyItem.Barcode(
            content = content,
            codeFormatted = content,
            copyLabel = copy.barcodeCopyLabel,
            copyFeedback = copy.barcodeCopyFeedback,
            imageDataUri = null,
            label = copy.barcodeLabel,
            icon = StatusScreenIcon.Copy,
        )
    }

private fun OrderProcessOutput.PaymentProcessed.isCardPayment(): Boolean =
    paymentMethod?.type.let { type -> type == null || type in CARD_PAYMENT_TYPES }

private fun OrderProcessOutput.PaymentProcessed.toPaymentRow(
    lastFourDigits: String?,
    subtitle: String?,
): StatusScreenBodyItem.ListItem? {
    val title = paymentTitle(lastFourDigits)
    if (title == null && subtitle == null) return null
    return StatusScreenBodyItem.ListItem(
        title = title,
        subtitle = subtitle,
        imageUrl = null,
        leadingType = null,
        leadingValue = null,
    )
}

private fun OrderProcessOutput.PaymentProcessed.paymentTitle(
    lastFourDigits: String?,
): String? =
    listOfNotNull(
        methodDisplayName(),
        lastFourDigits.orNullIfBlank()?.let { "•••• $it" },
    )
        .joinToString(separator = " ")
        .orNullIfBlank()
