package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_FOUR
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_SIX

/**
 * ExpirationCodeDateFormat enum that`s pass the input length and mask
 * @param digits input length
 * @param mask input mask
 */
enum class ExpirationDateFormat(val digits: Int, val mask: String) {
    /**
     * ShortFormat with two digits date format
     */
    ShortFormat(INT_FOUR, "##/##"),
    /**
     * LongFormat with four digits date format
     */
    LongFormat(INT_SIX, "##/####"),
}
