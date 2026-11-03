package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

internal data class StatusScreenFooter(
    val buttons: List<StatusScreenFooterButton>,
)

internal sealed class StatusScreenFooterButton {
    abstract val label: String
    abstract val style: StatusScreenButtonStyle?

    data class Back(
        override val label: String,
        override val style: StatusScreenButtonStyle? = null,
    ) : StatusScreenFooterButton()

    data class OpenUrl(
        override val label: String,
        val url: String,
        override val style: StatusScreenButtonStyle? = null,
    ) : StatusScreenFooterButton()

    data class OpenPdf(
        override val label: String,
        val receiptUrl: String,
        override val style: StatusScreenButtonStyle? = null,
    ) : StatusScreenFooterButton()

    data class ChangePaymentMethod(
        override val label: String,
        override val style: StatusScreenButtonStyle? = null,
    ) : StatusScreenFooterButton()
}

internal enum class StatusScreenButtonStyle {
    Loud,
    Quiet,
    Transparent,
}
