package com.mercadopago.sdk.android.coremethods.domain.usecase.validations

import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_NINE
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_ONE
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TEN
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TWO
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_ZERO
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.ZERO_CHAR


internal class IsCardNumberValidUseCase {
    operator fun invoke(cardNumber: String): Boolean {
        if (cardNumber.isEmpty()) return false

        var checksum: Int = INT_ZERO

        for (i in cardNumber.length - INT_ONE downTo INT_ZERO step INT_TWO) {
            checksum += cardNumber[i] - ZERO_CHAR
        }
        for (i in cardNumber.length - INT_TWO downTo INT_ZERO step INT_TWO) {
            val n: Int = (cardNumber[i] - ZERO_CHAR) * INT_TWO
            checksum += if (n > INT_NINE) n - INT_NINE else n
        }

        return checksum % INT_TEN == INT_ZERO
    }
}
