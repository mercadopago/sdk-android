package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntTwo
import java.util.Calendar

@Suppress("ReturnCount")
internal class IsExpirationDateValidUseCase {
    operator fun invoke(expirationDate: String, maxLength: Int): Boolean {
        if (expirationDate.isEmpty()) return false
        // TODO - Verify it this make sense
        if (expirationDate.length < maxLength) return false

        // TODO - Change this verification
        val currentDate =
            Calendar.getInstance().get(Calendar.YEAR).toString().takeLast(IntTwo).toInt()
        val actualDate = expirationDate.takeLast(IntTwo).toInt()

        return actualDate >= currentDate
    }
}
