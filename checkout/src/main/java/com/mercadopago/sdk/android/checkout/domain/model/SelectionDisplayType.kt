package com.mercadopago.sdk.android.checkout.domain.model

import kotlinx.serialization.Serializable

@Serializable
internal enum class SelectionDisplayType {
    Chevron,
    RadioButton,
}
