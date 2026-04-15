package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.core.utils.KoverIgnore

@KoverIgnore("in development")
internal fun CardType.toAnalyticsString(): String =
    when (this) {
        CardType.CREDIT -> "credit"
        CardType.DEBIT -> "debit"
        CardType.PREPAID -> "prepaid"
    }
