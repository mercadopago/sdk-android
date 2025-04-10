package com.mercadopago.sdk.android.coremethods.domain.usecase.validations

import com.mercadopago.sdk.android.coremethods.extensions.between
import com.mercadopago.sdk.android.coremethods.extensions.takeLast
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_ONE
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TWO
import java.util.Calendar

private const val MAX_MONTH = 12

internal class IsExpirationDateValidUseCase {
    operator fun invoke(
        expirationDate: String,
        maxLength: Int,
    ): Boolean {
        if (expirationDate.isEmpty()) return false

        return when (expirationDate.length) {
            maxLength -> {
                val firstSegment = expirationDate.take(INT_TWO).toInt()
                val lastSegmentDate = expirationDate.takeLast(INT_TWO).toInt()
                val year = Calendar.getInstance().get(Calendar.YEAR).takeLast(INT_TWO)
                val month = Calendar.getInstance().get(Calendar.MONTH) + INT_ONE

                when (lastSegmentDate >= year) {
                    true -> {
                        firstSegment.between(INT_ONE, MAX_MONTH) >= month.between(INT_ONE, MAX_MONTH)
                    }

                    else -> {
                        false
                    }
                }
            }

            else -> {
                false
            }
        }
    }
}
