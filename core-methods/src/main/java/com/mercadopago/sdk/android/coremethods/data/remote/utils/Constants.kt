package com.mercadopago.sdk.android.coremethods.data.remote.utils

import com.mercadopago.sdk.android.coremethods.BuildConfig

internal const val PRODUCT_ID: String = BuildConfig.CORE_METHODS_PRODUCT_ID

internal const val EXPIRATION_YEAR_START = "20"
internal const val EXPIRATION_YEAR_MIN_LENGTH = 2
internal const val SECURITY_CODE_MIN_LENGTH = 3
internal const val CARD_BIN_LENGTH = 8

internal const val ERROR_SECURITY_CODE_MIN_LENGTH = "security code length cannot be smaller than tree"
internal const val ERROR_EXPIRATION_DATE_EMPTY = "expiration date cannot be empty"
internal const val ERROR_EXPIRATION_DATE_LENGTH = "expiration date length cannot be smaller than two"
