package com.mercadopago.sdk.android.checkout.domain.model

internal fun SelectionDisplayType.toTrackingValue(): String =
    when (this) {
        SelectionDisplayType.Chevron -> "arrow"
        SelectionDisplayType.RadioButton -> "radio_button"
    }
