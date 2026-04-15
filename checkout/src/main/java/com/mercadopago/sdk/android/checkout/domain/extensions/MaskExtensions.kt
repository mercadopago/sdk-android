package com.mercadopago.sdk.android.checkout.domain.extensions

private const val CARD_LENGTH_8 = 8
private const val CARD_LENGTH_9 = 9
private const val CARD_LENGTH_10 = 10
private const val CARD_LENGTH_11 = 11
private const val CARD_LENGTH_12 = 12
private const val CARD_LENGTH_13 = 13
private const val CARD_LENGTH_14 = 14
private const val CARD_LENGTH_15 = 15
private const val CARD_LENGTH_16 = 16
private const val CARD_LENGTH_17 = 17
internal const val CARD_LENGTH_19 = 19

private const val CARD_LENGTH_8_MASK = "#### ####"
private const val CARD_LENGTH_9_MASK = "#### #####"
private const val CARD_LENGTH_10_MASK = "#### ######"
private const val CARD_LENGTH_11_MASK = "#### #### ###"
private const val CARD_LENGTH_12_MASK = "#### #### ####"
private const val CARD_LENGTH_13_MASK = "#### ###### ###"
private const val CARD_LENGTH_14_MASK = "#### ###### ####"
private const val CARD_LENGTH_15_MASK = "#### ###### #####"
private const val CARD_LENGTH_16_MASK = "#### #### #### ####"
private const val CARD_LENGTH_17_MASK = "#### #### #### #####"
internal const val CARD_LENGTH_19_MASK = "#### #### #### #### ###"

internal fun Int.toMask(): String =
    when (this) {
        CARD_LENGTH_8 -> CARD_LENGTH_8_MASK
        CARD_LENGTH_9 -> CARD_LENGTH_9_MASK
        CARD_LENGTH_10 -> CARD_LENGTH_10_MASK
        CARD_LENGTH_11 -> CARD_LENGTH_11_MASK
        CARD_LENGTH_12 -> CARD_LENGTH_12_MASK
        CARD_LENGTH_13 -> CARD_LENGTH_13_MASK
        CARD_LENGTH_14 -> CARD_LENGTH_14_MASK
        CARD_LENGTH_15 -> CARD_LENGTH_15_MASK
        CARD_LENGTH_16 -> CARD_LENGTH_16_MASK
        CARD_LENGTH_17 -> CARD_LENGTH_17_MASK
        CARD_LENGTH_19 -> CARD_LENGTH_19_MASK
        else -> CARD_LENGTH_16_MASK
    }
