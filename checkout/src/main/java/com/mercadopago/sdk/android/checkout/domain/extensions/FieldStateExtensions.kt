package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState

internal fun CardNumberState.isComplete() = length == maxLength
