package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_FOUR
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_SIX

/**
 * ExpirationCodeDateFormat enum that`s pass the input length and mask
 * @property ShortFormat four code digits and date format
 * @property LongFormat six code digits and date format
 */
enum class ExpirationDateFormat(val digits: Int, val mask: String) {
    ShortFormat(INT_FOUR, "##/##"),
    LongFormat(INT_SIX, "##/####"),
}
