package com.mercadopago.sdk.android.checkout.presentation.state

import kotlinx.serialization.Serializable

@Serializable
internal enum class InstallmentsDisplayType {
    Chevron,
    RadioButton,
}
