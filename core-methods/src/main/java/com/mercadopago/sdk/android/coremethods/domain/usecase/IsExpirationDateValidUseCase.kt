package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntTwo
import java.util.Calendar

private const val MinusYear = 190

internal class IsExpirationDateValidUseCase {
    operator fun invoke(expirationDate: String): Boolean {
        if (expirationDate.isEmpty()) return false

        // TODO - change this verification
        val currentDate = Calendar.getInstance().get(Calendar.YEAR) - MinusYear
        val actualDate = expirationDate.dropLast(IntTwo).toInt()

        return actualDate >= currentDate
    }
}
