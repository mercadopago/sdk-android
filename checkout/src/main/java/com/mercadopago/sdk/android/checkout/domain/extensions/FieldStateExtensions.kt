package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState

internal fun CardNumberState.isComplete() = length == maxLength

internal fun IdentificationTypeState.isComplete(
    length: Int,
) = selected?.maxLength == length || selected?.minLength == length
