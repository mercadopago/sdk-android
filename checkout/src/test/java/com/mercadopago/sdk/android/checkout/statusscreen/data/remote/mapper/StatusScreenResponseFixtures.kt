package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyDataResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyNodeResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenButtonResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenFooterResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenHeaderResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenResponse

internal fun sellerListItem() = StatusScreenBodyNodeResponse(
    component = "MPListItem",
    data = StatusScreenBodyDataResponse(
        title = "Seller name",
        subtitle = null,
        imageUrl = "https://http2.mlstatic.com/storage/cho_off-store-default",
        leadingType = null,
        leadingValue = null,
        content = null,
        codeFormatted = null,
        copyLabel = null,
        copyFeedback = null,
        imageDataUri = null,
    ),
)

internal fun paymentListItem() = StatusScreenBodyNodeResponse(
    component = "MPListItem",
    data = StatusScreenBodyDataResponse(
        title = "Visa Crédito •••• 1234 · 3x $ 300",
        subtitle = "$ 900 sin interés",
        imageUrl = null,
        leadingType = "icon",
        leadingValue = "card",
        content = null,
        codeFormatted = null,
        copyLabel = null,
        copyFeedback = null,
        imageDataUri = null,
    ),
)

internal fun barcodeNode(
    imageDataUri: String? = null,
) = StatusScreenBodyNodeResponse(
    component = "MPBarcode",
    data = StatusScreenBodyDataResponse(
        title = null,
        subtitle = null,
        imageUrl = null,
        leadingType = null,
        leadingValue = null,
        content = "34191.75537 05496.638292 71000.063305 1 96430000090000",
        codeFormatted = "34191.75537 05496.638292 71000.063305 1 96430000090000",
        copyLabel = "Copiar código",
        copyFeedback = "Código copiado",
        imageDataUri = imageDataUri,
    ),
)

internal fun footer(
    buttons: List<StatusScreenButtonResponse> = listOf(
        StatusScreenButtonResponse(label = "Volver", action = "back_action", receiptUrl = null),
        StatusScreenButtonResponse(
            label = "Ver factura",
            action = "open_pdf",
            receiptUrl = "https://www.mercadopago.com/receipt/abc",
        ),
    ),
) = StatusScreenFooterResponse(buttons = buttons)

internal fun approvedResponse(
    body: List<StatusScreenBodyNodeResponse> = listOf(sellerListItem(), paymentListItem()),
    footerResponse: StatusScreenFooterResponse = footer(),
    header: StatusScreenHeaderResponse? = StatusScreenHeaderResponse(
        title = "Pagaste ($ 900)",
        icon = "https://http2.mlstatic.com/storage/cho_off-feedback-success",
    ),
    statusType: String? = "approved",
) = StatusScreenResponse(statusType = statusType, header = header, body = body, footer = footerResponse)

internal fun changePaymentMethodButton(
    receiptUrl: String? = null,
    value: String? = null,
) = StatusScreenButtonResponse(
    label = "Pagar con otro medio",
    action = "change_payment_method",
    receiptUrl = receiptUrl,
    value = value,
    style = "loud",
)

internal fun backButton() = StatusScreenButtonResponse(
    label = "Volver",
    action = "back_action",
    receiptUrl = null,
    style = "transparent",
)

internal fun rejectedResponse(
    canRetry: Boolean?,
    buttons: List<StatusScreenButtonResponse> = listOf(changePaymentMethodButton(), backButton()),
    body: List<StatusScreenBodyNodeResponse> = listOf(
        sellerListItem(),
        paymentListItem(),
        messageNode("Usá otro medio de pago o intentá de nuevo."),
    ),
) = approvedResponse(
    statusType = "rejected",
    body = body,
    footerResponse = footer(buttons = buttons),
).copy(canRetry = canRetry)

internal fun messageNode(
    text: String? = "Estamos revisando tu pago",
) = StatusScreenBodyNodeResponse(
    component = "MPMessage",
    data = StatusScreenBodyDataResponse(
        title = null,
        subtitle = null,
        imageUrl = null,
        leadingType = null,
        leadingValue = null,
        content = null,
        codeFormatted = null,
        copyLabel = null,
        copyFeedback = null,
        imageDataUri = null,
        text = text,
    ),
)
