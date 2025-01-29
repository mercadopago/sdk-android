package com.mercadopago.sdk.android.coremethods.domain.usecase

private const val ZERO_CHAR = '0'
private const val BASE_10 = 10
private const val ZERO = 0
private const val ONE = 1
private const val TWO = 2
private const val NINE = 9

internal class IsCardNumberValidUseCase {
    operator fun invoke(cardNumber: String): Boolean {
        if (cardNumber.isEmpty()) return false

        var checksum: Int = ZERO

        for (i in cardNumber.length - ONE downTo ZERO step TWO) {
            checksum += cardNumber[i] - ZERO_CHAR
        }
        for (i in cardNumber.length - TWO downTo ZERO step TWO) {
            val n: Int = (cardNumber[i] - ZERO_CHAR) * TWO
            checksum += if (n > NINE) n - NINE else n
        }

        return checksum % BASE_10 == ZERO
    }
}
