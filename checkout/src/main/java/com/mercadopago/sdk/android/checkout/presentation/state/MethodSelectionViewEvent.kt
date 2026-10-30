package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption

internal sealed interface MethodSelectionViewEvent {
    data class OnOptionSelected(val option: MethodSelectionOption) : MethodSelectionViewEvent
}
