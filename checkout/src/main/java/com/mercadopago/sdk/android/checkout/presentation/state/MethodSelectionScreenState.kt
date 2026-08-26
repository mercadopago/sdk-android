package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState

internal data class MethodSelectionScreenState(
    val headerTitle: String,
    val options: List<MethodSelectionOption>,
    val selectedOptionId: String?,
    val footerState: FooterState,
    val isArrowLayout: Boolean,
)
