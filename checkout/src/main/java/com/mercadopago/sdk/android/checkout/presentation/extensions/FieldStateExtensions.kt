package com.mercadopago.sdk.android.checkout.presentation.extensions

import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState

internal fun CardNumberState.checkAllSameDigits(): Boolean {
    if (this.length != this.maxLength) return false
    val fullNumber = this.cardBin.orEmpty() + this.lastFourDigits
    return fullNumber.hasAllSameDigits()
}
