package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState

internal data class PaymentBrickScreenState(
    val title: String = "",
    val sections: List<PaymentSectionState> = emptyList(),
    val footerState: FooterState? = null,
    val pendingInstallmentData: MPInstallmentData? = null,
    val isLoading: Boolean = false,
)

internal data class PaymentSectionState(
    val title: String,
    val options: List<PaymentOptionState>,
)

internal data class PaymentOptionState(
    val id: String,
    val title: String,
    val thumbnailUrl: String? = null,
    val description: String? = null,
)
