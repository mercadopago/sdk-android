package com.mercadopago.sdk.android.coremethods.domain.usecase

private const val ZeroChar = '0'
private const val Base10 = 10
private const val ZERO = 0
private const val ONE = 1
private const val TWO = 2
private const val NINE = 9

internal class IsCardNumberValidUseCase {

    operator fun invoke(cardNumber: String): Boolean {
        if (cardNumber.isEmpty()) return false

        var checksum: Int = ZERO

        for (i in cardNumber.length - ONE downTo ZERO step TWO) {
            checksum += cardNumber[i] - ZeroChar
        }
        for (i in cardNumber.length - TWO downTo ZERO step TWO) {
            val n: Int = (cardNumber[i] - ZeroChar) * TWO
            checksum += if (n > NINE) n - NINE else n
        }

        return checksum % Base10 == ZERO
    }
}
