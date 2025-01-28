package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntFour
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntSix

/**
 * ExpirationCodeDateFormat enum that`s pass the input length and mask
 * @property ShortFormat four code digits and date format
 * @property LongFormat six code digits and date format
 */
enum class ExpirationCodeDateFormat(val digits: Int, val mask: String) {
    ShortFormat(IntFour, "##/##"),
    LongFormat(IntSix, "##/####")
}
