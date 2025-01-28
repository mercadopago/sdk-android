package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.extensions.between
import com.mercadopago.sdk.android.coremethods.extensions.takeLast
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntOne
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntTwo
import java.util.Calendar

private const val MaxMonth = 12

internal class IsExpirationDateValidUseCase {
    operator fun invoke(expirationDate: String, maxLength: Int): Boolean {
        if (expirationDate.isEmpty()) return false

        return when (expirationDate.length) {
            maxLength -> {
                val firstSegment = expirationDate.take(IntTwo).toInt()
                val lastSegmentDate = expirationDate.takeLast(IntTwo).toInt()
                val year = Calendar.getInstance().get(Calendar.YEAR).takeLast(IntTwo)
                val month = Calendar.getInstance().get(Calendar.MONTH)

                when (lastSegmentDate >= year) {
                    true -> {
                        firstSegment.between(IntOne, MaxMonth) <= month.between(IntOne, MaxMonth)
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
