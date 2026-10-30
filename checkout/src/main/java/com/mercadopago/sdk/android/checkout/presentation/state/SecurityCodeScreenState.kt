package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState

internal data class SecurityCodeScreenState(
    val title: String = "",
    val securityCodeState: SecurityCodeState = SecurityCodeState(),
    val footerState: FooterState = FooterState(),
    val fieldError: String? = null,
    val cardTitle: String = "",
    val cardDescription: String? = null,
    val cardImageUrl: String? = null,
)
