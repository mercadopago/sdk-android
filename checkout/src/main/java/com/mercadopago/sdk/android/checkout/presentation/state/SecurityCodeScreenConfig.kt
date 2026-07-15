package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState

internal data class SecurityCodeScreenConfig(
    val title: String,
    val securityCodeState: SecurityCodeState,
    val footerState: FooterState,
    val cardId: String,
    val cardTitle: String,
    val cardDescription: String? = null,
    val cardImageUrl: String? = null,
)
