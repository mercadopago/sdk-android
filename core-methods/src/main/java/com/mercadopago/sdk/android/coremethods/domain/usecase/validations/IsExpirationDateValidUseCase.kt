package com.mercadopago.sdk.android.coremethods.domain.usecase.validations

import com.mercadopago.sdk.android.coremethods.extensions.takeLast
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_ONE
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TWO
import java.util.Calendar

private const val MAX_MONTH = 12

internal class IsExpirationDateValidUseCase {
    @Suppress("ReturnCount")
    operator fun invoke(
        expirationDate: String,
        maxLength: Int,
    ): Boolean {
        return when (expirationDate.length) {
            maxLength -> {
                val month = expirationDate.take(INT_TWO).toIntOrNull() ?: return false
                val year = expirationDate.takeLast(INT_TWO).toIntOrNull() ?: return false
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR).takeLast(INT_TWO)
                val currentMonth = calendar.get(Calendar.MONTH) + INT_ONE

                if (month !in INT_ONE..MAX_MONTH) {
                    return false
                }

                return when {
                    year > currentYear -> true
                    year == currentYear -> month >= currentMonth
                    else -> false
                }
            }

            else -> false
        }
    }
}
